package org.cardanofoundation.ledgersync.job.schedules;

import com.bloxbean.cardano.client.crypto.Blake2bUtil;
import com.bloxbean.cardano.client.util.HexUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.cardanofoundation.explorer.consumercommon.entity.AssetMetadata;
import org.cardanofoundation.ledgersync.job.mapper.AssetMedataMapper;
import org.cardanofoundation.ledgersync.job.repository.AssetMetadataRepository;
import org.cardanofoundation.ledgersync.job.util.DataUtil;
import org.cardanofoundation.ledgersync.schedulecommon.dto.AssetMetadataDTO;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Log4j2
@ConditionalOnProperty(value = "jobs.meta-data.enabled", matchIfMissing = true, havingValue = "true")
public class AssetMetadataSchedule {

    private final AssetMetadataRepository assetMetadataRepository;
    private final AssetMedataMapper assetMedataMapper;

    @Value("${token.metadata.url}")
    private String url;
    @Value("${token.metadata.folder}")
    private String metadataFolder;
    @Value("${application.network}")
    private String network;

    @Transactional
    @Scheduled(fixedRate = 2000000, initialDelay = 2000)
    public void syncMetaData() throws IOException, GitAPIException {
        String pathFolder = cloneTokenMetadataRepo();
        List<AssetMetadataDTO> assetMetadataList = readTokenMetadata(pathFolder);
        saveTokenMetadata(assetMetadataList);
    }

    /**
     * Save token metadata to database
     *
     * @param assetMetadataDTOList list token metadata
     */
    private void saveTokenMetadata(List<AssetMetadataDTO> assetMetadataDTOList) {
        log.info("Save {} token metadata to database", assetMetadataDTOList.size());
        var currentTime = System.currentTimeMillis();
        // Map AssetMetadata source from database
        Map<String, AssetMetadata> assetMetadataMapSource = assetMetadataRepository.findAll().stream()
                .collect(Collectors.toMap(AssetMetadata::getSubject, Function.identity()));

        // List AssetMetadata need to save to database
        List<AssetMetadata> assetMetadataList = new ArrayList<>();

        assetMetadataDTOList.forEach(assetMetadataDTO -> {
            // Check if token metadata exist then update
            var assetMetadataTarget = assetMedataMapper.fromDTO(assetMetadataDTO);
            // Generate logo hash
            var logoHash = generateLogoHash(assetMetadataDTO);
            assetMetadataTarget.setLogoHash(logoHash);
            if (assetMetadataMapSource.containsKey(assetMetadataDTO.getSubject())) {
                var assetMetadataSource = assetMetadataMapSource.get(assetMetadataDTO.getSubject());
                assetMetadataTarget.setId(assetMetadataSource.getId());
            }
            assetMetadataTarget.setLogo(assetMetadataTarget.getLogo());
            assetMetadataList.add(assetMetadataTarget);
        });
        log.info("Processing raw data done!! Time taken: {} ms",
                System.currentTimeMillis() - currentTime);
        assetMetadataRepository.saveAll(assetMetadataList);
        log.info("Done save {} token metadata to database", assetMetadataList.size());
    }

    /**
     * Go through all files in token-metadata repo and read json file to AssetMetadataDTO
     *
     * @param pathFolder path to token-metadata repo
     * @return List<AssetMetadataDTO>
     * @throws IOException
     */
    private List<AssetMetadataDTO> readTokenMetadata(String pathFolder) throws IOException {
        List<AssetMetadataDTO> assetMetadataList = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(pathFolder + metadataFolder))) {
            paths.filter(Files::isRegularFile).forEach(file -> {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    Reader reader = Files.newBufferedReader(file);
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    AssetMetadataDTO assetMetadataDTO = mapper.readValue(reader, AssetMetadataDTO.class);
                    log.info("Crawl token: {}", assetMetadataDTO.getName().getValue());
                    log.info(file.getFileName().toString());
                    if (file.getFileName().toString().equals(assetMetadataDTO.getSubject().concat(".json"))
                            && assetMetadataDTO.getSubject().length() >= 56) {
                        assetMetadataList.add(assetMetadataDTO);
                    }
                    reader.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }
        return assetMetadataList;
    }

    /**
     * Clone or pull token-metadata repo
     *
     * @return path to token-metadata repo
     * @throws GitAPIException
     * @throws IOException
     */
    private String cloneTokenMetadataRepo() throws GitAPIException, IOException {
        String pathFolder = "./token-metadata-" + network;
        log.info("Clone metadata repository: " + url);
        File folder = new File(pathFolder);
        if (!folder.exists()) {
            Git git = Git.cloneRepository().setURI(url).setDirectory(folder).call();
            if (git != null) {
                log.info("Clone metadata repository done");
            } else {
                log.error("Clone metadata repository error");
            }
        } else {
            try (Git git = Git.open(folder)) {
                PullResult pull = git.pull().call();
                if (pull.isSuccessful()) {
                    log.info("Pull metadata repository done");
                } else {
                    log.error("Pull metadata repository error");
                }
            }
        }
        return pathFolder;
    }

    /**
     * Generate logo hash from AssetMetadataDTO
     *
     * @param assetMetadataDTO
     * @return null if AssetMetadataDTO.logo is null or encoded logo hash
     */
    private String generateLogoHash(AssetMetadataDTO assetMetadataDTO) {
        if (DataUtil.isNullOrEmpty(assetMetadataDTO.getLogo())
                || DataUtil.isNullOrEmpty(assetMetadataDTO.getLogo().getValue())) {
            return null;
        }
        String hash = network + "/" + assetMetadataDTO.getLogo().getValue();
        return HexUtil.encodeHexString(Blake2bUtil.blake2bHash256(hash.getBytes()));
    }
}
