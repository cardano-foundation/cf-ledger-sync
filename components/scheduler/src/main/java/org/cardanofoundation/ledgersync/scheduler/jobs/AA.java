package org.cardanofoundation.ledgersync.scheduler.jobs;

import static java.lang.Thread.sleep;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import org.cardanofoundation.ledgersync.scheduler.service.OffChainDataStoringService;
import org.cardanofoundation.ledgersync.scheduler.service.OffChainRetryDataErrorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

    //@Configuration
//@EnableScheduling
    @RestController
    @RequiredArgsConstructor
    public class AA
//    implements SchedulingConfigurer
    {

        private final OffChainDataStoringService offChainDataStoringService;
        private final OffChainRetryDataErrorService offChainRetryDataErrorService;
        static Long[] a = {1L, 5L, 20L, 10L};
        AtomicLong randomTime = new AtomicLong(3000);

        @GetMapping("/job")
        public String importAffiliateCSV(){
//            offChainDataStoringService.validateAndPersistData();

            offChainRetryDataErrorService.retryOffChainErrorData();
            return "done";
        }
//    @Bean(destroyMethod = "shutdown")
//    public Executor taskExecutor() {
//        return Executors.newScheduledThreadPool(5);
//    }

//    @Override
//    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//        taskRegistrar.setScheduler(taskExecutor());
//        taskRegistrar.addTriggerTask(
//            new Runnable() {
//                @Override
//                public void run() {
//                    System.out.println("run runnable ===  " + randomTime);
//                }
//            },
//            context -> {
//                randomTime = a[new Random().nextInt(a.length)] * 1000;
//                Instant lastCompletionTime = context.lastCompletion();
//                Instant nextExecutionTime =
//                    Objects.requireNonNullElse(lastCompletionTime, new Date().toInstant()
//                        .plusMillis(randomTime));
//                return Date.from(nextExecutionTime).toInstant();
//            }
//        );
//    }

        //    @Override
//        public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//            try {
//            taskRegistrar.setScheduler(taskExecutor());
//            taskRegistrar.addTriggerTask(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        randomTime = new AtomicLong(a[new Random().nextInt(a.length)] * 1000);
//                        System.out.println("run runnable ===  " + randomTime.get());
//                    }
//                },
//                context -> {
//                    System.out.println("Next execution in: " + randomTime + " seconds");
//                    Instant lastCompletionTime = context.lastCompletion();
//                    Instant nextExecutionTime =
//                        Objects.requireNonNullElse(lastCompletionTime, new Date().toInstant()
//                            .plusMillis(randomTime.get()));
//                    return Date.from(nextExecutionTime).toInstant();
//                }
//            );
//                taskRegistrar.addFixedDelayTask(new Runnable() {
//                    @Override
//                    public void run() {
//                        System.out.println("run runnable ===  5 second");
//                        System.out.println(new Date());
//                    }
//                }, Duration.ofSeconds(5));
//                taskRegistrar.addFixedDelayTask(new Runnable() {
//                    @Override
//                    public void run() {
//                        System.out.println("run runnable ===  7sec");
//                        System.out.println(new Date());
//                    }
//                }, Duration.ofSeconds(7));
//            taskRegistrar.addFixedDelayTask(cronJobWorkerProcess, Duration.ofSeconds(5));
//            taskRegistrar.addFixedDelayTask(cronJobWorkerRecover, Duration.ofSeconds(7));
//            } catch(Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//
//    }
//
//    private static final Charset UTF_8 = StandardCharsets.UTF_8;
//    private static final String OUTPUT_FORMAT = "%-20s:%s";
//
//    static final int TIMEOUT = 30000;
//    static final int READ_TIMEOUT = 19000;
//    static final int WRITE_TIMEOUT = 10000;
//    static final int LIMIT_BYTES = 4096;
//
////    public static byte[] digest(byte[] input, String algorithm) {
////        MessageDigest md;
////        try {
////            md = MessageDigest.getInstance(algorithm);
////        } catch (NoSuchAlgorithmException e) {
////            throw new IllegalArgumentException(e);
////        }
////        byte[] result = md.digest(input);
////        return result;
////    }
//
//    public static String bytesToHex(byte[] bytes) {
//        StringBuilder sb = new StringBuilder();
//        for (byte b : bytes) {
//            sb.append(String.format("%02x", b));
//        }
//        return sb.toString();
//    }
//

//    public static void main(String[] args) {
////        //String algorithm = "SHA-256"; // if you perfer SHA-2
////        String algorithm = "result";
////
////        String pText = "";
////        System.out.println(String.format(OUTPUT_FORMAT, "Input (string)", pText));
////        System.out.println(String.format(OUTPUT_FORMAT, "Input (length)", pText.length()));
////
////        byte[] shaInBytes = Blake2bUtil.blake2bHash256(pText.getBytes(UTF_8));
////        System.out.println(String.format(OUTPUT_FORMAT, algorithm + " (hex) ", bytesToHex(shaInBytes)));
////        // fixed length, 32 bytes, 256 bits.
////        System.out.println(String.format(OUTPUT_FORMAT, algorithm + " (length)", shaInBytes.length));
////
////
//        Book book = new Book();
//        book.setBookName("Java Reference");
//        book.setDescription("will not be saved");
//        book.setCopies(25);
//
//        try {
////            serialize(book);
//            deserialize();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static void serialize(Book book) throws Exception {
//        FileOutputStream file = new FileOutputStream("fileName.txt");
//        ObjectOutputStream out = new ObjectOutputStream(file);
//        out.writeObject(book);
//        out.close();
//        file.close();
//    }
//
//    public static Book deserialize() throws Exception {
//        FileInputStream file = new FileInputStream("/Users/duczau/Desktop/quanle/cf-ledger-sync/fileName.txt");
//        ObjectInputStream in = new ObjectInputStream(file);
//        Book book = (Book) in.readObject();
//        in.close();
//        file.close();
//        return book;
//    }
//
//    private static WebClient buildWebClient() throws SSLException {
//        var sslContext =
//            SslContextBuilder.forClient()
//                .sslProvider(SslProvider.JDK)
//                .trustManager(InsecureTrustManagerFactory.INSTANCE)
//                .startTls(true)
//                .build();
//
//        var httpClient =
//            HttpClient.create()
//                .wiretap(Boolean.FALSE)
//                .secure(t -> t.sslContext(sslContext))
//                .followRedirect(Boolean.TRUE)
//                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
//                .responseTimeout(Duration.ofMillis(TIMEOUT))
//                .doOnConnected(
//                    connection -> {
//                        connection.addHandlerFirst(
//                            new ReadTimeoutHandler(READ_TIMEOUT, TimeUnit.MILLISECONDS));
//                        connection.addHandlerFirst(
//                            new WriteTimeoutHandler(WRITE_TIMEOUT, TimeUnit.MILLISECONDS));
//                    });
//
//        return WebClient.builder()
//            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//            .clientConnector(new ReactorClientHttpConnector(httpClient))
//            .build();
//    }


}
