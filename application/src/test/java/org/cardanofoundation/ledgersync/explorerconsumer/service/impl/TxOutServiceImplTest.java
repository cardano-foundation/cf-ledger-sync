package org.cardanofoundation.ledgersync.explorerconsumer.service.impl;

import com.bloxbean.cardano.yaci.core.model.*;
import org.cardanofoundation.explorer.consumercommon.entity.Datum;
import org.cardanofoundation.explorer.consumercommon.entity.*;
import org.cardanofoundation.explorer.consumercommon.enumeration.TokenType;
import org.cardanofoundation.ledgersync.common.util.HexUtil;
import org.cardanofoundation.ledgersync.common.util.JsonUtil;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedAddress;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTx;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTxIn;
import org.cardanofoundation.ledgersync.explorerconsumer.aggregate.AggregatedTxOut;
import org.cardanofoundation.ledgersync.explorerconsumer.dto.EUTXOWrapper;
import org.cardanofoundation.ledgersync.explorerconsumer.projection.TxOutProjection;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.FailedTxOutRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.MultiAssetTxOutRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.repository.TxOutRepository;
import org.cardanofoundation.ledgersync.explorerconsumer.service.MultiAssetService;
import org.cardanofoundation.ledgersync.explorerconsumer.service.ScriptService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigInteger;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TxOutServiceImplTest {

    @Mock
    private ScriptService scriptService;
    @Mock
    private MultiAssetService multiAssetService;
    @Mock
    private TxOutRepository txOutRepository;
    @Mock
    private FailedTxOutRepository failedTxOutRepository;
    @Mock
    private MultiAssetTxOutRepository multiAssetTxOutRepository;
    @Captor
    private ArgumentCaptor<List<FailedTxOut>> failedTxOutCaptor;
    @Captor
    private ArgumentCaptor<List<TxOut>> txOutCaptor;
    @Captor
    private ArgumentCaptor<MultiValueMap<String, MaTxOut>> maTxOutCaptor;

    private TxOutServiceImpl victim;

    @BeforeEach
    void setUp() throws Exception {
        victim = new TxOutServiceImpl(scriptService, multiAssetService, txOutRepository,
                failedTxOutRepository, multiAssetTxOutRepository);
    }

    @Test
    void shouldReturnEmptyTxOutWhenTxInsEmpty() {
        Collection<AggregatedTxIn> emptyTxIns = Collections.emptyList();
        Collection<TxOut> result = victim.getTxOutCanUseByAggregatedTxIns(emptyTxIns);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnTxOutsSuccessfully() {
        final Collection<AggregatedTxIn> txIns = List.of(new AggregatedTxIn(1,
                "e42c3bcea73dcd464d3744f88b7820b91881273c515d0a4b408694942d898a05", 4));
        final List<TxOutProjection> txOutProjections = List.of(
                new TxOutProjection(30055291L, "e42c3bcea73dcd464d3744f88b7820b91881273c515d0a4b408694942d898a05", 12930101L,
                        (short) 1, new BigInteger("2034438"), 1L,
                        "addr1w8433zk2shufk42hn4x7zznjjuqwwyfmxffcjszw5l2ulesdt3jff", true,
                        "eb188aca85f89b55579d4de10a729700e7113b325389404ea7d5cfe6"));
        when(txOutRepository.findTxOutsByTxHashInAndTxIndexIn(any()))
                .thenReturn(txOutProjections);

        final Collection<TxOut> result = victim.getTxOutCanUseByAggregatedTxIns(txIns);
        List<TxOut> txOuts = result.stream().toList();
        Assertions.assertEquals("e42c3bcea73dcd464d3744f88b7820b91881273c515d0a4b408694942d898a05",
                txOuts.get(0).getTx().getHash());
        Assertions.assertEquals(12930101L, txOuts.get(0).getTx().getId());
        Assertions.assertEquals((short) 1, txOuts.get(0).getIndex());
        Assertions.assertEquals("addr1w8433zk2shufk42hn4x7zznjjuqwwyfmxffcjszw5l2ulesdt3jff",
                txOuts.get(0).getAddress());
        Assertions.assertEquals(true, txOuts.get(0).getAddressHasScript());
        Assertions.assertEquals("eb188aca85f89b55579d4de10a729700e7113b325389404ea7d5cfe6",
                txOuts.get(0).getPaymentCred());
        Assertions.assertEquals(1L, txOuts.get(0).getStakeAddress().getId());
        Assertions.assertEquals(new BigInteger("2034438"), txOuts.get(0).getValue());
    }

    @Test
    void shouldReturnEUTXOWrapperWithEmptyFieldsWhenAggregatedTxOutMapIsEmpty() {
        final Map<String, List<AggregatedTxOut>> aggregatedTxOutMap = Collections.emptyMap();
        final Map<String, Tx> txMap = givenTxMap();
        final Map<String, StakeAddress> stakeAddressMap = givenStakeAddressMap();
        final Map<String, Datum> datumMap = givenDatumMap("15461aa490b224fe541f3568e5d7704e0d88460cde9f418f700e2b6864d8d3c9");
        EUTXOWrapper result = victim.prepareTxOuts(aggregatedTxOutMap, txMap, stakeAddressMap, datumMap);
        Mockito.verifyNoInteractions(txOutRepository);
        Mockito.verifyNoInteractions(multiAssetService);
        Mockito.verifyNoInteractions(multiAssetTxOutRepository);
        Assertions.assertEquals(0, result.getMaTxOuts().size());
        Assertions.assertEquals(0, result.getTxOuts().size());
    }

    @Test
    void shouldPrepareTxOutSuccessfully() {
        final AggregatedTxOut aggregatedTxOut = givenAggregatedTxOut();
        final Map<String, List<AggregatedTxOut>> aggregatedTxOutMap = Map.ofEntries(
                Map.entry("4143afe2f4b17537f73e7c95f3fcf2fbfe35d0eb95b91336891bbe547e22ef7d", List.of(aggregatedTxOut)));
        final Map<String, Tx> txMap = givenTxMap();
        final Map<String, StakeAddress> stakeAddressMap = givenStakeAddressMap();
        final Map<String, Datum> datumMap = givenDatumMap(aggregatedTxOut.getInlineDatum().getHash());
        Mockito.when(scriptService.getHashOfReferenceScript(anyString())).thenReturn("hash");
        Mockito.when(scriptService.getScriptsByHashes(any())).thenReturn(Map.of("hash",
                Script.builder().hash("hash").build()));
        MultiValueMap<String, MaTxOut> maTxOutMultiValueMap = new LinkedMultiValueMap<>();
        var maTxOut = MaTxOut.builder()
                .quantity(BigInteger.valueOf(5517481))
                .build();
        maTxOutMultiValueMap.add("asset1sjk0uucljv4qxxnhq8gjy7r5mar64erhfuh4q8", maTxOut);
        Mockito.when(multiAssetService.buildMaTxOut(any(), any())).thenReturn(maTxOutMultiValueMap);
        Mockito.when(multiAssetService.updateIdentMaTxOuts(any())).thenReturn(List.of(MaTxOut.builder().ident(
                        MultiAsset.builder().fingerprint("asset1sjk0uucljv4qxxnhq8gjy7r5mar64erhfuh4q8")
                                .policy("659ab0b5658687c2e74cd10dba8244015b713bf503b90557769d77a7")
                                .nameView("WingRiders")
                                .build())
                .build()));

        victim.prepareTxOuts(aggregatedTxOutMap, txMap, stakeAddressMap, datumMap);

        Mockito.verify(txOutRepository, Mockito.times(1)).saveAll(txOutCaptor.capture());
        Mockito.verify(multiAssetService, Mockito.times(1)).updateIdentMaTxOuts(maTxOutCaptor.capture());
        Mockito.verify(multiAssetTxOutRepository, Mockito.times(1)).saveAll(any());
        List<TxOut> txOutsForSaving = txOutCaptor.getValue();
        MultiValueMap<String, MaTxOut> maTxOutMultiValueMapForUpdating = maTxOutCaptor.getValue();
        Assertions.assertEquals(aggregatedTxOut.getAddress().getAddress(),
                txOutsForSaving.get(0).getAddress());
        Assertions.assertEquals(aggregatedTxOut.getIndex().shortValue(), txOutsForSaving.get(0).getIndex());
        Assertions.assertEquals(aggregatedTxOut.getDatumHash(), txOutsForSaving.get(0).getDataHash());
        Assertions.assertEquals(aggregatedTxOut.getDatumHash(), txOutsForSaving.get(0).getDataHash());
        Assertions.assertEquals(aggregatedTxOut.getAddress().getPaymentCred(), txOutsForSaving.get(0).getPaymentCred());
        Assertions.assertEquals(aggregatedTxOut.getAddress().getAddressRaw(), txOutsForSaving.get(0).getAddressRaw());
        Assertions.assertEquals(aggregatedTxOut.getAddress().isAddressHasScript(), txOutsForSaving.get(0).getAddressHasScript());
        Assertions.assertEquals(aggregatedTxOut.getNativeAmount(), txOutsForSaving.get(0).getValue());
        Assertions.assertEquals(TokenType.ALL_TOKEN_TYPE, txOutsForSaving.get(0).getTokenType());
        Assertions.assertEquals(aggregatedTxOut.getInlineDatum().getHash(), txOutsForSaving.get(0).getInlineDatum().getHash());
        Assertions.assertEquals("hash", txOutsForSaving.get(0).getReferenceScript().getHash());
        Assertions.assertEquals(maTxOutMultiValueMap.get("asset1sjk0uucljv4qxxnhq8gjy7r5mar64erhfuh4q8"),
                maTxOutMultiValueMapForUpdating.get("asset1sjk0uucljv4qxxnhq8gjy7r5mar64erhfuh4q8"));
    }

    private AggregatedTxOut givenAggregatedTxOut() {
        return AggregatedTxOut.builder()
                .index(0)
                .address(AggregatedAddress.builder()
                        .address("addr_test1qqwh05w69g95065zlr4fef4yfpjkv8r3dr9h3pu0egy5n7pwhxp4f95svdjr9dmtqumqcs6v49s6pe7ap4h2nv8rcaasgrkndk")
                        .addressRaw("AB130doqC0fqgvjqnKakSGVmHHFoy3iHj8oJSfguuYNUlpBjZDK3awc2DENMqWGg590Nbqmw48d7".getBytes())
                        .paymentCred("1d77d1da2a0b47ea82f8ea9ca6a44865661c7168cb78878fca0949f8")
                        .addressHasScript(false)
                        .stakeReference(HexUtil.decodeHexString("e02eb983549690636432b76b07360c434ca961a0e7dd0d6ea9b0e3c77b"))
                        .build())
                .nativeAmount(new BigInteger("100"))
                .amounts(List.of(Amount.builder()
                        .assetName("WingRiders")
                        .policyId("659ab0b5658687c2e74cd10dba8244015b713bf503b90557769d77a7")
                        .unit("lovelace")
                        .quantity(BigInteger.valueOf(5517481))
                        .build()))
                .scriptRef("82025918ad5918aa01000032323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232223232323232325335533355333573466e1d202800103815333573460b0002264244460020086eb8d5d0982f0010a999ab9a305700113232122230020045333573460b060c00022646464642466600200a0060046eb4d5d09aba2002375c6ae84004d5d118300011bae3574260be0020a86ea8d5d0982f0010298a99a998079a80282424411c428ee1664ae069f2e86714b4bdcacb587c56976f1debc8d612b7e456000401335738920104453034350003f213500122253355333573466e20005208092f40113357389201044530333300043044132323253355333573466e20008cdc1241e002907a01823899ab9c4901044530333400046153355335330163500c04f0050471335738920104453033350004615335353500c04f05c1301501922153350011301701b221533500113300500400222153350011330070040022215335001133009004002221301f02304504522323253355335350042222533533355304f053301801a23500122333553052056301b01d23500122333500123302f480000040b88cc0bc00520000013355304a053235001223304c002333500123355304e057235001223305000230320010012233302e02c00200123355304e0572350012233050002303300100133302902700300615335330230040051533533024303533700012900118168040270a99ab9c4901044531313400161533573892010445313135001604c04a1335738921044530333000049153355335333301e500e00900700304a133573892010445303331000491533533020302f008302900304a13357389210445303332000490480483500522220043500420423333333333333530240010510542052054051054054051051051054200105433301404f003500704223213232333333333333353023002051054205205422533533020002001130030021533573892010445303433001605405405105105105425333573466e2400520f4031300200115335738921044530343200160542325335353500a04d05a13012017221533500113003002221533500113005002221533500113007002221301a01f2533553353333019050005337006aa008444004906046db80800822899ab9c491044530333700044132533553353301c3302c3370400200208a604a00408c266ae712410445303338000451533553353301b350022222004350042222004046133573892104453033390004515333573466e1cd540140fc00c1184cd5ce2481044530343000045044044333355304604a01023700a666a660cc446464a666ae68cdc419b820020014800010054ccd5cd183080109809a40002a666ae68cdc380100089809a40042a666ae68cdc400100089813a40002a666ae68cdc400124000260086609a60c000460c000226660d4444a666ae68cdc399b823370400400400800a2602c0042a666ae68cdc380099b80002480084c0a80084c94ccd5cd19b893370466e080040040140184cc0100040084cc01000c004cdc199b8000200148011200233700900119b830020013500204b350010503304900148008840048400454cd5ce24902453000163355304104a23500104835500403c4800010cccc04813400940144c8cccd54c10c11c03488d400888c8d4010888c8c94ccd5cd19b89002480004ccc15801401000c4ccccccccccccd4c0ac01c168174816c8c008cc158cc120005401c1501680041741681681681741741748ccc158cdc000280119b80004500633553048051235001223304a00233350012001700400266604e04a0020066a00a444466a666aa609e0a604246a002444a666ae68cdc78018088a999ab9a3371e004024260980080a00a060a800646a00208c900009a8009111198351119a800a4000446a00444a666ae68cdc7801244100133070223350014800088d4008894ccd5cd19b8f0024890010011300600300113006003003500533304c4800120000495005112001153357389201035054350016305e001375400c26a6a00408a44444444444401826a002446666a0020a440020a40a4a666ae68c148c1680044c8c8c848cc00400c008c0b4d5d09aba2305b0035333573460a660b6002264646464646464646464646464646464646464646464642466666666666600203002c02802402001c01801200e00a006004608e6ae84d5d100119981fbae2001357420026ae88008ccc0f40fc8004d5d08009aba20023303c75c6ae84004d5d1001a999ab9a3062306a00113232323212330010040025333573460ca60da002264642466002006004606e6ae84d5d118368011981bbad3574260d80020c26ea8d5d09aba2306b0035333573460c660d6002264642466002006004606a6ae84d5d118358011981abad3574260d40020be6ea8d5d0983480082f1baa357420026ae88008ccc0d80e9d69aba1001357440046606a06e6ae84004d5d10011998193ae031357420026ae88008ccc0c1d70179aba1001357440046605e0566ae84004d5d1001198168141aba10013574460b60046605604c6ae84c16800413cdd51aba1305900104e375400242444600400846606444666a0060820040026a00207266060444600660040024002246600244a66a004200206a0784a66ae7124104453034310016253357389201044530333600162235002222222222222533533355304004401225333573466e3c0380044c0e80040f801081000f48894cd4ccd54c0dc0e0894cd4d4d40080dc88894cd4c0fc00c0dc884d40088894cd40100f08854cd400454ccd5cd19b8f00501015333573466e3c01003c4ccd5cd182c00182381f81f01f1102009981f001000880081d00089802002910a99a80089a80101b91098040049299ab9c490104453131330016498cc0a0894cd40088400c40040b08888d400488894cd4c0e800c0c8884d40088894cd40100dc884d40088894cd401054ccd5cd19b8700101015333573466e3c02004854ccd5cd19b8f0070111333573460ac00c08a07a0780780784407c446a004446a006446466a00a466a0084a666ae68cdc78010008018191019919a802101992999ab9a3371e0040020060642a66a00642a66a0044266a004466a004466a004466a004466020004002406c466a004406c46602000400244406c44466a008406c444a666ae68cdc38030018a999ab9a3370e00a00426602600800206e06e0602a66a0024060062446666666666666a004446666666666666a00644a666ae68cdc380200109980500180081710171110181017110179017101711017910179101790171017101711999999999999a80111017119804001000911017901691017101690169101711017110171016901690169111999999999999a8021101810179112999ab9a3370e00c0062a666ae68cdc38028010998060020008180181017910181017901791018110181101810179017901791999999999999a8011101710169110179198038010009101710169016910171101711017101690169016911999999999999a8019101790171110181017112999ab9a3370e00800426601400600205c405c405c4405e4405e4405e405c405c405c46666666666666a0044405c405a44405e405a4405c46600a004002405a4405c4405c4405c405a405a405a46666666666666a0044405c405a44405e405a4405c405a46600a0040024405c4405c4405c405a405a405a446666666666666a0064405e405c444060405c4405e405c405c44a666ae68cdc38020010a99a80188008a99a8008170180171101791017901710171017111999999999999a8019101790171110181017110179017101711017912999ab9a3370e00800426601000600205c4405e405c405c405c446666666666666a0064405e405c444060405c4405e405c405c4405e4405e44a666ae68cdc380200109980400180081710171017101711999999999999a8011101710169110179016910171016901691017110171101711a8011111a8021112999ab9a3371e00c0062a666ae68cdc78028010998070020008190191016901691999999999999a801110171016911017901691017101690169101711017110171016919804001000901691999999999999a801110171016911017901691017101690169101711017110171016901691980400100091199a9814018111a801111a801912999ab9a3371e00800426601600600205e00400244666a604e05e446a004446a00644a666ae68cdc780200109a8019111a8021112999ab9a3370e00c0062a666ae68cdc380280109980800200081a01a01700100091199ab9a3371e00400206005044666ae68cdc3801000817813911199aa981601681899aa981201691a80091198130011804000999aa9816016911a80111299a999aa9816818998139119980501b001000980401711a8009119805001002803080189981a80200181780099aa981201691a80091198130011982591299a800898058019109a80111299a9980600100408911198010050020980300180200111980091299a801013880081210911180180210911180080211a80091112999a80101c10a99a9999999aba400123232325333573466e1d2018002133335573e6092006405646666aae7cd5d118250021299a9998078088071aba1304b00521302812222222222222300800e02b202c03103015333573466e1d2016002133335573e6092006405646666aae7cd5d118250021299a98089aba1304b005213028301300102b202c03103015333573466e1d2014002133335573e6092006405646666aae7cd5d118250021299a9998078088071aba1304b00521302812222222222222300700e02b202c03103015333573466e1d2012002133335573e6092006405646666aae7d400880b08cccd55cf9aba2500325335300f35742609800c4264a66a6666666ae900048c8c94ccd5cd182580089999aab9f30510022302e03b2033038153335734609400226666aae7cc1440088c0b80cc80cc0e00c4c144004dd5001101810181018101801a9098159980b8018008171aba1500502c202d03203103015333573466e1d2010002133335573e6092006405646666aae7d400880b08cccd55cf9aba2500325335300f35742609800c42a66a60206ae85401484c0a848888888888888cc03403c0380b40b080b40c80c40c054ccd5cd19b87480380084cccd55cf9824801901591999aab9f5002202c233335573e6ae89400c94cd4c03cd5d0982600310a99a98081aba1500521302a122222222222223300900f00e02d02c202d032031030153335734609000426666aae7cc12400c80ac8cccd55cf9aba2304a00425335300e35742609600a426050602a00205640580620602a666ae68c11c0084cccd55cf9824801901591999aab9f357446094008464a66a6666666ae900048c94ccd5cd182398278008991999aab9f001203123233335573e00240664646666aae7c00480d48cccd55cf9aba200225335301b3574200e42a66a60386ae84018854cd4c068d5d080290981a0919980080200180101b81b01a901b01d81d1aba20020383574400406c609c00205c6ea800880b880b880b880b80cc84c0a448888888888888c00c0380b0d5d0982580290160188180a999ab9a3046002133335573e6092006405646666aae7d400880b08c8cccd55cf800901711999aab9f357440044a66a60226ae84c138020854cd4c048d5d0a80390a99a98099aba100521302d1222222222222233300b01000f00e03002f02e202f03403335744a0060620602a666ae68c1140084cccd55cf9824801901591999aab9f5002202c233335573e6ae89400c94cd4c03cd5d0982600310a99a9998088098079aba1500521302a122222222222223300400f00e02d02c202d032031030153335734608800426666aae7cc12400c80ac8cccd55cfa801101611999aab9f35744a0064a66a601e6ae84c130018854cd4ccc04404c03cd5d0a802909815091111111111111980280780701681610168190188180a999ab9a3043002133335573e6092006405646666aae7cd5d118250021299a98071aba1304b00521302812222222222222300100e02b202c031030153335734608400426666aae7cc12400c80ac8cccd55cf9aba2304a00425335300e35742609600a42605024444444444444601801c056405806206005226ae88c120008c120004dd50011013901390139013816108008a99ab9c491044531313100162039233333335748002464a666ae68c0e8c1080044c8cccd55cf80090121191999aab9f001202623233335573e002405046666aae7cd5d10011299a98059aba1007215335300c3574200c42a66a601a6ae8401484c09c48ccc00401000c0080a80a40a080a40b80b4d5d10010159aba200202930410010213754004404240424042404204c46666666ae900048080808080808c06cdd680110100129111999999aba400100425335330422233335573e0024603e058464a66a6010608a00242a66a6010608a00442a66a600c6ae8801484c08ccc0c8cc09000c008004098094090d5d08010149bab00221301d001020004004004026201d233333335748002403a403a403a403a460306eb8008088848888888888888c0280388848888888888888cc01803c038848888888888888c0080388c94ccd5cd181900080d0a999ab9a303100101802d30383754002446464a666ae68c0d000404454ccd5cd18198008990911180180218021aba1303900215333573460640022244400405c60720026ea80048c94ccd5cd1817981b80089919091980080180118021aba135744606e00460186ae84c0d80040acdd50009192999ab9a302e303600113232323232323232321233330010090070030023300b75c6ae84d5d10022999ab9a3037001132122230020043574260780042a666ae68c0d80044c84888c004010dd71aba1303c002153335734606a00202c06260780026ea8d5d08009aba200233300875c00e6ae84004d5d1181b801a999ab9a302f30370011323212330010030023300500b357426ae88c0dc008c02cd5d0981b0008159baa35742606a0020546ea800488c8c94ccd5cd18178008980918021aba13036002153335734606000202c056606c0026ea8004cc005d73ad222330352233335573e002403e4646604466028600e6070002600c606e00260086ae8800cd5d080100e1bab00122330332233335573e002403a46603e600a6ae84008c00cd5d100100d1bac0012323253335734605a00226424444600800a60086ae84c0c400854ccd5cd181600089909111180100298029aba13031002153335734605600226424444600200a600e6ae84c0c400854ccd5cd18150008990911118018029bae35742606200404c60620026ea80048c8c94ccd5cd18178008891110078a999ab9a302e001112222222004153335734605a0022646424444444660020120106eb4d5d09aba23031003375c6ae84c0c000854ccd5cd18160008991909111111198010048041bae357426ae88c0c400cdd71aba13030002153335734605600226464244444446600c0120106eb8d5d09aba2303100330043574260600042a666ae68c0a80044c848888888c01c020c010d5d098180010a999ab9a3029001132122222223005008300435742606000404a60600026ea80048c8c94ccd5cd18148008991919190911998008030020019bad357426ae88008dd69aba10013574460600066eb4d5d098178010a999ab9a30280011321223002003300435742605e004048605e0026ea80048c8c94ccd5cd181400089909118008019bae35742605c0042a666ae68c09c0044c8488c00800cdd71aba1302e002023302e0013754002464a666ae68c094c0b40044c8c848cc00400c008dd69aba135744605a00460066ae84c0b0004084dd50009192999ab9a3024302c0011375c6ae84c0ac004080dd50008880091100088800911001909118010019109198008018011812110891299a80080411099809980200119aa98030078020009811910911299a8010a99a80080411004110a99a801804110a99a998038020010999a98048088038018008050800880188804180f9108911299a80089a801803110999a8028071802001199aa980380580280200091000980e9112999ab9a30170011615333573466e20005200013300330160023016001132323212330010030023370800600466e1000c004ccc0808894ccd5cd180d000880109980180099b86002001002001301c22112225335001100222133005002333553007008005004001120013301a2225335001100222135002223300733330212222533500110022213500222533357346040002266601000e00c006266601000e6602466602600e00400200c00600400c0020060022002440044424466002008006444246660020080060049111cdb6809173313ce44bf4461b17dcd8a6657dc58ae3995962e9fd20fd30020042004200425335738920104453032390016253357389201044530343400162533573892010445313136001615335738920104453131320016153357389201035054310016253357389201024c68001622222222222200a370290001b8748000dc3a40046e1d2004370e90031b8748020dc3a40146e1d200c5573caae748c8c00400488cc00cc008008005")
                .inlineDatum(com.bloxbean.cardano.yaci.core.model.Datum.builder()
                        .cbor("01").json("").build())
                .build();
    }


    @Test
    void shouldHandleFailedTxOutsSuccessfullyWhenSuccessTxHaveNullCollateralReturn() {
        final var failedTx = givenFailedTxs(true);
        final var successTx = givenSuccessTxs(false);
        final Map<String, Tx> txMap = givenTxMap();
        final Map<String, StakeAddress> stakeAddressMap = givenStakeAddressMap();
        final Map<String, Datum> datumMap = givenDatumMap("15461aa490b224fe541f3568e5d7704e0d88460cde9f418f700e2b6864d8d3c9");

        victim.handleFailedTxOuts(List.of(successTx), List.of(failedTx), txMap, stakeAddressMap, datumMap);

        Mockito.verify(failedTxOutRepository, Mockito.times(1)).saveAll(failedTxOutCaptor.capture());
        Assertions.assertEquals(1, failedTxOutCaptor.getValue().size());
        var failedTxOutForSaving = failedTxOutCaptor.getValue().get(0);
        var txOut = failedTx.getTxOutputs().get(0);

        Assertions.assertEquals(txOut.getIndex(), failedTxOutForSaving.getIndex().shortValue());
        Assertions.assertEquals(txOut.getDatumHash(), failedTxOutForSaving.getDataHash());
        Assertions.assertEquals(txOut.getNativeAmount(), failedTxOutForSaving.getValue());
        Assertions.assertEquals(JsonUtil.getPrettyJson(txOut.getAmounts()), failedTxOutForSaving.getMultiAssetsDescr());

        var addressOfFailTxOut = txOut.getAddress();

        Assertions.assertEquals(addressOfFailTxOut.getPaymentCred(), failedTxOutForSaving.getPaymentCred());
        Assertions.assertEquals(addressOfFailTxOut.isAddressHasScript(), failedTxOutForSaving.getAddressHasScript());
        Assertions.assertEquals(addressOfFailTxOut.getAddressRaw(), failedTxOutForSaving.getAddressRaw());

        Assertions.assertEquals(txMap.get(failedTx.getHash()).getHash(), failedTxOutForSaving.getTx().getHash());
        Assertions.assertEquals(stakeAddressMap.get(addressOfFailTxOut.getStakeAddress()).getHashRaw(), failedTxOutForSaving.getStakeAddress().getHashRaw());
    }

    @Test
    void shouldHandleFailedTxOutsSuccessfullyWhenFailedTxHaveEmptyTxOutputs() {
        final var failedTx = givenFailedTxs(false);
        final var successTx = givenSuccessTxs(true);
        final Map<String, Tx> txMap = givenTxMap();
        final Map<String, StakeAddress> stakeAddressMap = givenStakeAddressMap();
        final Map<String, Datum> datumMap = givenDatumMap("15461aa490b224fe541f3568e5d7704e0d88460cde9f418f700e2b6864d8d3c9");

        victim.handleFailedTxOuts(List.of(successTx), List.of(failedTx), txMap, stakeAddressMap, datumMap);

        Mockito.verify(failedTxOutRepository, Mockito.times(1)).saveAll(failedTxOutCaptor.capture());
        Assertions.assertEquals(1, failedTxOutCaptor.getValue().size());
        var failedTxOutForSaving = failedTxOutCaptor.getValue().get(0);
        var txOut = successTx.getCollateralReturn();

        Assertions.assertEquals(txOut.getIndex(), failedTxOutForSaving.getIndex().shortValue());
        Assertions.assertEquals(txOut.getDatumHash(), failedTxOutForSaving.getDataHash());
        Assertions.assertEquals(txOut.getNativeAmount(), failedTxOutForSaving.getValue());
        Assertions.assertEquals(JsonUtil.getPrettyJson(txOut.getAmounts()), failedTxOutForSaving.getMultiAssetsDescr());

        var addressOfFailTxOut = txOut.getAddress();

        Assertions.assertEquals(addressOfFailTxOut.getPaymentCred(), failedTxOutForSaving.getPaymentCred());
        Assertions.assertEquals(addressOfFailTxOut.isAddressHasScript(), failedTxOutForSaving.getAddressHasScript());
        Assertions.assertEquals(addressOfFailTxOut.getAddressRaw(), failedTxOutForSaving.getAddressRaw());

        Assertions.assertEquals(txMap.get(successTx.getHash()).getHash(), failedTxOutForSaving.getTx().getHash());
        Assertions.assertEquals(stakeAddressMap.get(addressOfFailTxOut.getStakeAddress()).getHashRaw(), failedTxOutForSaving.getStakeAddress().getHashRaw());
    }

    @Test
    void shouldHandleFailedTxOutsSuccessfully() {
        AggregatedTx failedTx = givenFailedTxs(true);
        AggregatedTx successTx = givenSuccessTxs(true);
        final Map<String, Tx> txMap = givenTxMap();
        final Map<String, StakeAddress> stakeAddressMap = givenStakeAddressMap();
        final Map<String, Datum> datumMap = givenDatumMap("15461aa490b224fe541f3568e5d7704e0d88460cde9f418f700e2b6864d8d3c9");

        victim.handleFailedTxOuts(List.of(successTx),  List.of(failedTx), txMap, stakeAddressMap, datumMap);

        Mockito.verify(failedTxOutRepository, Mockito.times(1)).saveAll(failedTxOutCaptor.capture());

        var failedTxOutListForSaving = failedTxOutCaptor.getValue();
        Assertions.assertEquals(2, failedTxOutCaptor.getValue().size());

        var txOutFromSuccessTxs = successTx.getCollateralReturn();
        var txOutFromFailedTxs = failedTx.getTxOutputs().get(0);
        for (var failedTxOutForSaving :
                failedTxOutListForSaving) {
            AggregatedTxOut txOut;
            if ((failedTxOutForSaving.getTx().getHash()).equals(successTx.getHash())) {
                txOut = txOutFromSuccessTxs;
            } else {
                txOut = txOutFromFailedTxs;
            }

            Assertions.assertEquals(txOut.getIndex(), failedTxOutForSaving.getIndex().shortValue());
            Assertions.assertEquals(txOut.getDatumHash(), failedTxOutForSaving.getDataHash());
            Assertions.assertEquals(txOut.getNativeAmount(), failedTxOutForSaving.getValue());
            Assertions.assertEquals(JsonUtil.getPrettyJson(txOut.getAmounts()), failedTxOutForSaving.getMultiAssetsDescr());

            var addressOfFailTxOut = txOut.getAddress();

            Assertions.assertEquals(addressOfFailTxOut.getPaymentCred(), failedTxOutForSaving.getPaymentCred());
            Assertions.assertEquals(addressOfFailTxOut.isAddressHasScript(), failedTxOutForSaving.getAddressHasScript());
            Assertions.assertEquals(addressOfFailTxOut.getAddressRaw(), failedTxOutForSaving.getAddressRaw());
            Assertions.assertEquals(stakeAddressMap.get(addressOfFailTxOut.getStakeAddress()).getHashRaw(), failedTxOutForSaving.getStakeAddress().getHashRaw());
        }

    }

    private AggregatedTx givenSuccessTxs(boolean hasCollateralReturn) {
        AggregatedTx aggregatedTx = Mockito.mock(AggregatedTx.class);
        Mockito.lenient().when(aggregatedTx.getHash()).thenReturn("15e319aeac14618de9ff37ce203ed4bd25c5c6c271312fcf296909b5e3fde8a8");
        if (hasCollateralReturn) {
            Mockito.when(aggregatedTx.getCollateralReturn()).thenReturn(AggregatedTxOut.builder()
                    .index(1)
                    .address(AggregatedAddress.builder()
                            .address("addr_test1qqwh05w69g95065zlr4fef4yfpjkv8r3dr9h3pu0egy5n7pwhxp4f95svdjr9dmtqumqcs6v49s6pe7ap4h2nv8rcaasgrkndk")
                            .addressRaw("AB130doqC0fqgvjqnKakSGVmHHFoy3iHj8oJSfguuYNUlpBjZDK3awc2DENMqWGg590Nbqmw48d7".getBytes())
                            .paymentCred("1d77d1da2a0b47ea82f8ea9ca6a44865661c7168cb78878fca0949f8")
                            .addressHasScript(false)
                            .stakeReference(HexUtil.decodeHexString("e02eb983549690636432b76b07360c434ca961a0e7dd0d6ea9b0e3c77b"))
                            .build())
                    .nativeAmount(new BigInteger("7000000"))
                    .amounts(List.of(Amount.builder()
                            .assetName("lovelace")
                            .quantity(BigInteger.valueOf(7000000))
                            .unit("lovelace")
                            .policyId(null)
                            .build()))
                    .datumHash(null)
                    .inlineDatum(null)
                    .scriptRef(null)
                    .build());
        }

        return aggregatedTx;
    }

    private AggregatedTx givenFailedTxs(boolean hasTxOutputs) {
        AggregatedTx aggregatedTx = Mockito.mock(AggregatedTx.class);
        Mockito.lenient().when(aggregatedTx.getHash()).thenReturn("4143afe2f4b17537f73e7c95f3fcf2fbfe35d0eb95b91336891bbe547e22ef7d");

        if (hasTxOutputs) {
            Mockito.when(aggregatedTx.getTxOutputs()).thenReturn(List.of(AggregatedTxOut.builder()
                    .index(0)
                    .address(AggregatedAddress.builder()
                            .address("addr_test1qqwh05w69g95065zlr4fef4yfpjkv8r3dr9h3pu0egy5n7pwhxp4f95svdjr9dmtqumqcs6v49s6pe7ap4h2nv8rcaasgrkndk")
                            .addressRaw("AB130doqC0fqgvjqnKakSGVmHHFoy3iHj8oJSfguuYNUlpBjZDK3awc2DENMqWGg590Nbqmw48d7".getBytes())
                            .paymentCred("1d77d1da2a0b47ea82f8ea9ca6a44865661c7168cb78878fca0949f8")
                            .addressHasScript(false)
                            .stakeReference(HexUtil.decodeHexString("e02eb983549690636432b76b07360c434ca961a0e7dd0d6ea9b0e3c77b"))
                            .build())
                    .nativeAmount(new BigInteger("100"))
                    .amounts(List.of(Amount.builder()
                            .assetName("lovelace")
                            .policyId(null)
                            .unit("lovelace")
                            .quantity(BigInteger.valueOf(48000000))
                            .build()))
                    .datumHash(null)
                    .inlineDatum(null)
                    .scriptRef(null)
                    .build()));
        }

        return aggregatedTx;
    }

    private Map<String, Tx> givenTxMap() {
        return Map.ofEntries(Map.entry("4143afe2f4b17537f73e7c95f3fcf2fbfe35d0eb95b91336891bbe547e22ef7d",
                        Tx.builder()
                                .hash("4143afe2f4b17537f73e7c95f3fcf2fbfe35d0eb95b91336891bbe547e22ef7d")
                                .validContract(false)
//                                .id(499718L)
                                .build()),
                Map.entry("15e319aeac14618de9ff37ce203ed4bd25c5c6c271312fcf296909b5e3fde8a8",
                        Tx.builder()
                                .hash("15e319aeac14618de9ff37ce203ed4bd25c5c6c271312fcf296909b5e3fde8a8")
                                .validContract(false)
//                                .id(499719L)
                                .build()));
    }

    private Map<String, Datum> givenDatumMap(String hash) {
        return Map.ofEntries(Map.entry(hash, Datum.builder()
//                .id(13343L)
                .hash(hash)
                .build()));
    }

    private Map<String, StakeAddress> givenStakeAddressMap() {
        return Map.ofEntries(Map.entry("e0d64c93ef0094f5c179e5380109ebeef022245944e3914f5bcca3a793", StakeAddress.builder()
//                        .id(6964L)
                        .view("stake_test1urtyeyl0qz20tsteu5uqzz0tamczyfzegn3ezn6mej360ycky7cg5")
                        .build()),
                Map.entry("f00b266918f9792dbcf750f9628c0ac4da65597fb6b8dfd0f83400c97d", StakeAddress.builder()
//                        .id(18952L)
                        .view("stake_test17q9jv6gcl9ujm08h2ruk9rq2cndx2ktlk6udl58cxsqvjlg975p77")
                        .build()),
                Map.entry("e02eb983549690636432b76b07360c434ca961a0e7dd0d6ea9b0e3c77b", StakeAddress.builder()
//                        .id(10001L)
                        .view("stake_test1uqhtnq65j6gxxepjka4swdsvgdx2jcdqulws6m4fkr3uw7ckxp06v")
                        .build())
        );
    }
}
