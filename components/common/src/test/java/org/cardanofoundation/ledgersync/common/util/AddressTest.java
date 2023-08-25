package org.cardanofoundation.ledgersync.common.util;

import com.bloxbean.cardano.client.crypto.Bech32;
import com.bloxbean.cardano.client.crypto.Blake2bUtil;
import org.cardanofoundation.ledgersync.common.common.address.ShelleyAddress;
import org.cardanofoundation.ledgersync.common.common.address.ShelleyAddressType;
import org.cardanofoundation.ledgersync.common.exception.AddressException;
import java.util.List;
import java.util.stream.IntStream;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AddressTest {

  @Test
  void TestDecodeBaseShellyAddressByBech32() throws AddressException {
    String addrTestnest = "addr_test1qr70jlnuh9fqxdmj3qt93u4pnhakfwaraxfh2krlwu7m0xg8wza5pajla3zhyfm4eqd5lac8pd6lpc9fjngpf04qmw5qqu7m6m";
    String expectStakeAddress = "stake_test1uqrhpw6q7e07c3tjya6usx607urska0suz5ef5q5h6sdh2qaw3909";
    ShelleyAddress addr = new ShelleyAddress(addrTestnest);
    Assertions.assertEquals(expectStakeAddress, addr.getBech32DelegationPart());
    System.out.println(addr.getHexPaymentPart());
  }

  @Test
  void TestPoolHash6000000Mainnet() {
    //block 6000000 mainnet
    String issureVkey = "bfab01b624c1989181542b4eefd87bac4f3186df06f8f01b03fa2de823355805";
    String poolHash = "pool18vej0s9gska8c847aj95g9v24duuxg2g63d5cuqngnxew643jw9";
    Assertions.assertEquals(poolHash,
        Bech32.encode(Blake2bUtil.blake2bHash224(Hex.decode(issureVkey)), "pool"));
  }

  @Test
  void TestAddressToStakeAndPaymentCred() {
    String addr = "addr_test1qzjlptv5m75xhqsuaed8t0ekzmnwc0fykhgd66g9eyp7na5vr8svw5cndlfh777apttwyjm042x54vxtrnutyppekc7qj09qmd";
    String expectStake = "stake_test1uzxpncx82vfkl5ml00ws44hzfdh64r22kr93e79jqsumv0q8g8cy0";
    String expectPaymentCred = "a5f0ad94dfa86b821cee5a75bf3616e6ec3d24b5d0dd6905c903e9f6";

    ShelleyAddress shelleyAddress = new ShelleyAddress(addr);
    Assertions.assertEquals(expectStake, shelleyAddress.getBech32DelegationPart());
    System.out.println(shelleyAddress.getShelleyAddressType());
    Assertions.assertEquals(expectPaymentCred, shelleyAddress.getHexPaymentPart());
  }

  @Test
  void TestnetAddressesHavePaymentAndStake() {
    String[] addresses = {
        "addr_test1qp4d2kgycnze40ga7avgxg8d0thjtsjdyawa2xppwh6fyq8v3qewx3mfqssgl0y6cj6dll38d000glrruq8nlpugcm7qjfszhg",
        "addr_test1qp564c54ll92uknusdytq4zet0rgeu08p4cq8eycfrt89pw8ca6u0mgvjkjv2hq2awmqvfczskug4hvzjg9c6m8uc08qnty2zt",
        "addr_test1qp73ljurtknpm5fgey5r2y9aympd33ksgw0f8rc5khheg83y35rncur9mjvs665cg4052985ry9rzzmqend9sqw0cdksxvefah",
        "addr_test1qpadhxqzsnychwdgmq6gwkjn8v586cyg32xd6xxvuf33qqpt2s7w05kg84sxyam7rfe9pue9wd2jf5dtgu5any7suraqhu2z2w",
        "addr_test1qpf5pkl7vzkv08vcg7gefsdts5nspp32zzkfsz9s9hl348nkv3p8m5knvn335ypcj9p0t9qrmgeyvgyf5ddxqmneekdq6u39fy",
        "addr_test1qpkk3glskjfy6zfw70nh089pf2ypkty57czkqy8xxsa48vsrc22yfyq4kfsvyhxxer06ya5jls8nj6rlyqx0kwc37nlsqu4eew",
        "addr_test1qplc6qr300gqg9n2vag4d8y9vhxm74dht9ztdwqwflc2cw8t8mkkrae2jsv2uldv6ur88h3vzdr6wm5g9ajwwwcsqucqnkwh2z",
        "addr_test1qps7xw5day7dql9plyhzxzf0npct02rtr5ems5wlhg7nfag9mrrrztuz6fmc8xnzw05xyue5su20v780vpcr57tfq4gsz9hkwf",
        "addr_test1qpu62wdwm0h3zw20nvexq4j6m34vp9l776ppz88udcx2fqu2nrvmwjr9w67t3xt9wnlvy5204jn29s4h3584wfcdymlqyhzuey",
        "addr_test1qpwk9d4cv725gz2k7trpkp7futa2erutfk6mfpyh8rpcpykn6sxqm23nlufe8ujmm45038nzjdzvgnl4rx4e0jq3jr4qmxm93x"};

    String[] stakeAddresses = {"stake_test1urkgsvhrga5sggy0hjdvfdxllcnkhhh50337qrels7yvdlqh05hp6",
        "stake_test1urruwaw8a5xftfx9ts9whdsxyupgtwy2mkpfyzuddn7v8nsgmh0q2",
        "stake_test1uqjg6peuwpjaexgdd2vy2h69zn6pjz33pdsvekjcq88uxmgc6qc7u",
        "stake_test1uq44g0886tyr6crzwalp5ujs7vjhx4fy6x45w2wej0gwp7sam5g8v",
        "stake_test1upmxgsna6tfkfcc6zqufzsh4jspa5vjxyzy6xknqdeuumxs90sxyy",
        "stake_test1uqpu99zyjq2mycxztnrv3hazw6f0creedpljqr8m8vglflcx66nxp",
        "stake_test1ur4namtp7u4fgx9w0kkdwpnnmckpx3a8d6yz7e888vgqwvq5vmuk8",
        "stake_test1uqza333397pdyaurnf3886rzwv6gw98k0rhkqup6095s25g4dmkqu",
        "stake_test1uz9f3kdhfpjhd09cn9jhflkz2986ef4zc2mc6r6hyuxjdls0xwacz",
        "stake_test1urfagrqd4gel7yun7fda668cne3fx3xyfl63n2uheqgep6sk73cxp"};

    String[] paymentCredHex = {
        "6ad55904c4c59abd1df7588320ed7aef25c24d275dd5182175f49200",
        "69aae295ffcaae5a7c8348b054595bc68cf1e70d7003e49848d67285",
        "7d1fcb835da61dd128c9283510bd26c2d8c6d0439e938f14b5ef941e",
        "7adb980284c98bb9a8d834875a533b287d60888a8cdd18cce2631000",
        "5340dbfe60acc79d98479194c1ab852700862a10ac9808b02dff1a9e",
        "6d68a3f0b4924d092ef3e7779ca14a881b2c94f6056010e6343b53b2",
        "7f8d00717bd004166a6751569c8565cdbf55b75944b6b80e4ff0ac38",
        "61e33a8de93cd07ca1f92e23092f9870b7a86b1d33b851dfba3d34f5",
        "79a539aedbef11394f9b3260565adc6ac097fef682111cfc6e0ca483",
        "5d62b6b86795440956f2c61b07c9e2faac8f8b4db5b4849738c38092"
    };

    for (int i = 0; i < addresses.length; i++) {
      ShelleyAddress shelleyAddress = new ShelleyAddress(addresses[i]);
      Assertions.assertTrue(shelleyAddress.containStakeAddress());
      Assertions.assertTrue(shelleyAddress.containPaymentCred());
      Assertions.assertTrue(ShelleyAddress.checkBech32HasStakeAddress(addresses[i]));
      Assertions.assertEquals(stakeAddresses[i],shelleyAddress.getBech32DelegationPart());
      Assertions.assertEquals(paymentCredHex[i],shelleyAddress.getHexPaymentPart());
    }
  }

  @Test
  void MainnetAddressesHavePaymentAndStake(){
    String[] addresses = {
        "addr1q800h57vhlprmcmy6aqfmad5vlms4susqspqmz5vajd4678nxq832z6zq568fqrg2hpzc3c9344gssejeluxll2lwkmsya902a",
        "addr1q800r2qjkxgpwjag46au74caa6lf60pprrh0ezj62lz8x0u9sny6qgcczu6krgluy9jdkf2pfa784k29927fggn0uvws88ldr5",
        "addr1q8024g3nr6mrhjl3569y3c2p2u29k7wjz5ht7dts0qq8kpszxkm5w0n40ly6hyc0p93hrh8s3a7upuws3tp2muay2vasnf2nck",
        "addr1q8040nfd43d5rsdlyk70emrxu4tgama6k66q34k4yp0ufhf65qnngwgvehc5d7nkkvzwth4ly7vhs3refff8pwrcwwzsuy24ef",
        "addr1q80lcf29y48tp8rw97slka4a886ynw8lv2nkphqyjm0sgfcu9656t7a64ecvqs62vrxvcmxcm2rlcsga9zej5p9kekhs58qd89",
        "addr1q80le4w9heejqzrxfkyxkfsflhv0rjfre3kxg0kaap7zrjsuyx7a4hat39yr82xw0h83qztjgm3fmtw629hpd36zkv7qnnw8ty",
        "addr1q80lsvvv4tf7ul7emtzp60ue4j7gv87l7nxqek8ez4qkq4x5uxd994r3wgtjzad87sy5ywmyrc36eqyhr3lkesh8ywfqyxl9wm",
        "addr1q80m0kj2xlfdpq9gs3f809ye2tw6d2f987f3swqlqfx8xpm4w3zfjadq5wne2996zs3cqz8kjc82m8wl06ucz2kkf3aqdg3gpr",
        "addr1q80rpphlkv2pr0s8cwgwtht0qv8hwtxwr0thahtnmc7w3n8mh3fpqy2tazxt0kkd4tgydfm7krn26lsfrmjsk4ljx84q9svxe6",
        "addr1q80s0qvg4mdcwrs3v9zq4yxxc2dud6tnyp22r2jeh4zqc2jcrk2md6p7hqu9vzedw8hf4uuwudrmlca4wau38y2jt4lqjdnltz"
    };

    String[] stakeAddresses = {
        "stake1u8enqrc4pdpq2dr5sp59ts3vguzc665ggvevl7r0l40htdcj583e3",
        "stake1uxzcfjdqyvvpwdtp507zzexmy4q57lr6m9zj40y5yfh7x8gx0sqpp",
        "stake1uyprtd688e6hljdtjv8sjcm3mncg7lwq78gg4s4d7wj9xwczfqgyq",
        "stake1uya2qfe58yxvmu2xlfmtxp89m6lj0xtcg3u555nshpu88pgly8eqe",
        "stake1uywza2d9lwa2uuxqgd9xpnxvdnvd4plugywj3ve2qjmvmtcstc9c7",
        "stake1uywzr0w6m74cjjpn4r88mncsp9eydc5a4hd9zmskcaptx0qrs2whd",
        "stake1u82wrxjj63chy9epwknlgz2z8djpugavszt3clmvctnj8ys3rht7h",
        "stake1u96hg3yewks28fu4zjapgguqprmfvr4dnh0hawvp9ttyc7s87z6q3",
        "stake1u8amc5ssz9973r9hmtx645zx5altpe4d0cy3aegt2lerr6s9jwp7v",
        "stake1u9vpm9dkaqltswzkpvkhrm567w8wx3aluw6hw7gnj9f96ls7cgvah"
    };

    String[] paymentCredHex = {
        "defbd3ccbfc23de364d7409df5b467f70ac39004020d8a8cec9b5d78",
        "def1a812b190174ba8aebbcf571deebe9d3c2118eefc8a5a57c4733f",
        "deaaa2331eb63bcbf1a68a48e14157145b79d2152ebf357078007b06",
        "df57cd2dac5b41c1bf25bcfcec66e5568eefbab6b408d6d5205fc4dd",
        "dffc2545254eb09c6e2fa1fb76bd39f449b8ff62a760dc0496df0427",
        "dffcd5c5be732008664d886b2609fdd8f1c923cc6c643edde87c21ca",
        "dff8318caad3ee7fd9dac41d3f99acbc861fdff4cc0cd8f915416054",
        "dfb7da4a37d2d080a8845277949952dda6a9253f9318381f024c7307",
        "de3086ffb31411be07c390e5dd6f030f772cce1bd77edd73de3ce8cc",
        "df078188aedb870e1161440a90c6c29bc6e9732054a1aa59bd440c2a"
    };

    for (int i = 0; i < addresses.length; i++) {
      ShelleyAddress shelleyAddress = new ShelleyAddress(addresses[i]);
      Assertions.assertTrue(shelleyAddress.containStakeAddress());
      Assertions.assertTrue(shelleyAddress.containPaymentCred());
      Assertions.assertTrue(ShelleyAddress.checkBech32HasStakeAddress(addresses[i]));
      Assertions.assertEquals(stakeAddresses[i],shelleyAddress.getBech32DelegationPart());
      Assertions.assertEquals(paymentCredHex[i],shelleyAddress.getHexPaymentPart());
    }
  }

  @Test
  void testStakeReferenceShouldNotBeNullAndHasScriptHashReference() {
    String address = "addr_test1xryzdtruqtz5chmnltzgugsxt3tna83kcxn6lfr7g7lja4hv28yp985rdnm3rdnj6jz7scfqmc39vjnd30vs7ffyqdns7tsmvc";
    ShelleyAddress shelleyAddress = new ShelleyAddress(address);
    Assertions.assertNotEquals(new byte[0], shelleyAddress.getStakeReference());

    ShelleyAddress stakeShelleyAddress = new ShelleyAddress(shelleyAddress.getStakeReference());
    Assertions.assertEquals(ShelleyAddressType.SCRIPT_REWARD,
        stakeShelleyAddress.getShelleyAddressType());
    Assertions.assertTrue(stakeShelleyAddress.hasScriptHashReference());
  }

  @Test
  void addressesShouldHaveCorrectStakeReference() {
    List<String> addresses = List.of(
        // Mainnet
        "addr1x8t6x30tat2zyp75xgtk8jqh9p5cgvj5eqwsql069flwy7wh5dz7h6k5ygragvshv0ypw2rfsse9fjqaqp7l52n7ufusnsfatl",
        "addr1xx2apgeq5umfvnuscwlja85fh0mcjmcx024v254x37dfavju22nejh0vdrq29g406x73umsy2zxns95zkd0rlege5fdqs0q89f",
        "addr1xx64m8gpwxklrf5z4tdpyuzrfqp04yfe39kz6krylzz6vz44tkwszudd7xng92k6zfcyxjqzl2gnnztv94vxf7y95c9q95ts8v",
        "addr1x935ex05y7082ng6d7r94nx2tpm52fyf295v78apkj2nrfmrfjvlgfu7w4x35muxttxv5krhg5jgj5tgeu06rdy4xxnsu9nj4d",
        "addr1x8ffychfh9aln5n837ehy4y2kstvz22vke0pl0kkqvlcw0wjjf3wnwtml8fx0ranwf2g4dqkcy55edj7r7ldvqelsu7s8w765m",
        "addr1xxn5m5qz8dur0qr0e9jlwp08mydql35r8lm0gv99vtd5vr48fhgqywmcx7qxljt97uz70kg6plrgx0lk7sc22ckmgc8qvktz2f",
        "addr1x8ppqql45pjs5gnwu0ce7epue8nvtw5dvqxqc6gvkwa9udkzzqpltgr9pg3xacl3najrej0xckag6cqvp35seva6tcmqljly8z",
        "addr1x93hhu7dknrxaqqjn9aqxvrl4mu9v6fx0txf2rnuuhdspwnr00eumdxxd6qp9xt6qvc8lthc2e5jv7kvj588eewmqzaq00hvrs",
        "addr1xxchhgvst9fvld3jk8s69dq6sa3fuyfnh307cz5cdgnkpj430wseqk2je7mr9v0p526p4pmzncgn80zlas9fs638vr9qrsz9yg",
        "addr1xyj8lp6rqx7tgzd00hmkqzuxut0s7s8j76dq34sm2tzly0fy07r5xqduksy67l0hvq9cdcklpaq09a56prtpk5k97g7sh045ja",

        // Testnet
        "addr_test1xryzdtruqtz5chmnltzgugsxt3tna83kcxn6lfr7g7lja4hv28yp985rdnm3rdnj6jz7scfqmc39vjnd30vs7ffyqdns7tsmvc",
        "addr_test1xz0pfsd43e0w97vahqg3ype3dvkqlermlk5hjqeenwnckf57znqmtrj7utuemwq3zgrnz6evplj8hldf0ypnnxa83vnqwv2s09",
        "addr_test1xpg7q28yhe54fns6c6ejw68sgvpalagftp4l55p2yhmehrj3uq5wf0nf2n8p434nya50qscrml6sjkrtlfgz5f0hnw8qwzxrj9",
        "addr_test1xq7srtmfqejhq6q623mm9gzqu9e8aascjt2qtutza45ruceaqxhkjpn9wp5p54rhk2sypctj0mmp3yk5qhck9mtg8e3sddr4m7",
        "addr_test1xpcqdrhvwvg5gv8mge6gjpl48lzsmemyyx0sn372eqqdjtrsq68wcuc3gsc0k3n53yrl2079phnkggvlp8ru4jqqmykq9kxg5x",
        "addr_test1xz5p4lx5qnnu8vez2nj0mnxfvnkv38uwdh2uswe9t4l9ck4grt7dgp88cwejy48ylhxvje8vez0cumw4eqaj2ht7t3dqm26v7d",
        "addr_test1xqax7xw5rwqmlm9cv59v0zn5jwunlas52mdt94jpkt87aye6duvagxuphlktseg2c798fyae8lmpg4k6kttyrvk0a6fsnuuqje",
        "addr_test1xp6p78scsjdsvv250c39874a4pkhys5s0mj9lfy0p95ffan5ru0p3pymqcc4gl3z20atm2rdwfpfqlhyt7jg7ztgjnmqzk3rch",
        "addr_test1xru27vj3ug40zxthfnmkk4p5wc0y6c0dh6symuffd34y2hlc4ue9rc327yvhwn8hdd2rgas7f4s7m04qfhcjjmr2g40s6t0xv0",
        "addr_test1xzqt8mwuxx83h3sjmsekknf0k080a7757zctxzuszmqxpvuqk0kacvv0r0rp9hpnddxjlv7wlmaafu9skv9eq9kqvzesyrurcg"
    );

    List<String> stakeReferences = List.of(
        // Mainnet
        "d7a345ebead42207d4321763c8172869843254c81d007dfa2a7ee279",
        "5c52a7995dec68c0a2a2afd1bd1e6e04508d381682b35e3fe519a25a",
        "b55d9d0171adf1a682aada1270434802fa9139896c2d5864f885a60a",
        "634c99f4279e754d1a6f865accca58774524895168cf1fa1b49531a7",
        "d29262e9b97bf9d2678fb372548ab416c1294cb65e1fbed6033f873d",
        "a74dd0023b7837806fc965f705e7d91a0fc6833ff6f430a562db460e",
        "c21003f5a0650a226ee3f19f643cc9e6c5ba8d600c0c690cb3ba5e36",
        "637bf3cdb4c66e8012997a03307faef85669267acc950e7ce5db00ba",
        "b17ba1905952cfb632b1e1a2b41a87629e1133bc5fec0a986a2760ca",
        "247f874301bcb409af7df7600b86e2df0f40f2f69a08d61b52c5f23d",

        // Testnet
        "ec51c8129e836cf711b672d485e86120de22564a6d8bd90f25240367",
        "9e14c1b58e5ee2f99db8111207316b2c0fe47bfda97903399ba78b26",
        "51e028e4be6954ce1ac6b32768f04303dff509586bfa502a25f79b8e",
        "3d01af69066570681a5477b2a040e1727ef61892d405f162ed683e63",
        "70068eec73114430fb46748907f53fc50de764219f09c7cac800d92c",
        "a81afcd404e7c3b32254e4fdccc964ecc89f8e6dd5c83b255d7e5c5a",
        "3a6f19d41b81bfecb8650ac78a7493b93ff61456dab2d641b2cfee93",
        "741f1e18849b0631547e2253fabda86d7242907ee45fa48f096894f6",
        "f8af3251e22af119774cf76b5434761e4d61edbea04df1296c6a455f",
        "80b3eddc318f1bc612dc336b4d2fb3cefefbd4f0b0b30b9016c060b3"
    );

    IntStream.range(0, addresses.size()).forEach(addrIdx -> {
      String b32Address = addresses.get(addrIdx);
      ShelleyAddress shelleyAddress = new ShelleyAddress(b32Address);
      Assertions.assertTrue(shelleyAddress.containStakeAddress());

      ShelleyAddress stakeAddress = new ShelleyAddress(shelleyAddress.getStakeReference());
      String stakeReference = HexUtil.encodeHexString(stakeAddress.getStakeReference());
      String actualStakeReference = stakeReferences.get(addrIdx);
      Assertions.assertEquals(actualStakeReference, stakeReference.substring(2));
    });
  }

  @Test
  void addressesShouldHaveScriptPaymentCred() {
    List<String> addresses = List.of(
        // Mainnet
        "addr1x8t6x30tat2zyp75xgtk8jqh9p5cgvj5eqwsql069flwy7wh5dz7h6k5ygragvshv0ypw2rfsse9fjqaqp7l52n7ufusnsfatl",
        "addr1zxe73cqte0ujaztctuspuqevufwyeluy026txta26gzcy8fkdjhdjguag5r7vxglt9dldzm7wwq908urplfkk8at2jgq88qk0w",
        "addr1zyxc268gfz0vswfktfgqd0zctev3zvr7rlknygcj4pmjal64l7skssf7apc5946qtv7jmv40930s3ljh35drn8ru6g3s0pg2ly",
        "addr1zyxc268gfz0vswfktfgqd0zctev3zvr7rlknygcj4pmjal64l7skssf7apc5946qtv7jmv40930s3ljh35drn8ru6g3s0pg2ly",
        "addr1xx2apgeq5umfvnuscwlja85fh0mcjmcx024v254x37dfavju22nejh0vdrq29g406x73umsy2zxns95zkd0rlege5fdqs0q89f",
        "addr1xx64m8gpwxklrf5z4tdpyuzrfqp04yfe39kz6krylzz6vz44tkwszudd7xng92k6zfcyxjqzl2gnnztv94vxf7y95c9q95ts8v",
        "addr1x935ex05y7082ng6d7r94nx2tpm52fyf295v78apkj2nrfmrfjvlgfu7w4x35muxttxv5krhg5jgj5tgeu06rdy4xxnsu9nj4d",
        "addr1x8ffychfh9aln5n837ehy4y2kstvz22vke0pl0kkqvlcw0wjjf3wnwtml8fx0ranwf2g4dqkcy55edj7r7ldvqelsu7s8w765m",
        "addr1z82rpyky9nxkq6e6a6hcsy8hwp8jdxvpwaz5lv3p4f3ce77ga0vyccwpssu2fn9vpttg938eu54k6t3598gpcf7yqslsrj832n",
        "addr1xxn5m5qz8dur0qr0e9jlwp08mydql35r8lm0gv99vtd5vr48fhgqywmcx7qxljt97uz70kg6plrgx0lk7sc22ckmgc8qvktz2f",

        // Testnet
        "addr_test1xryzdtruqtz5chmnltzgugsxt3tna83kcxn6lfr7g7lja4hv28yp985rdnm3rdnj6jz7scfqmc39vjnd30vs7ffyqdns7tsmvc",
        "addr_test1xz0pfsd43e0w97vahqg3ype3dvkqlermlk5hjqeenwnckf57znqmtrj7utuemwq3zgrnz6evplj8hldf0ypnnxa83vnqwv2s09",
        "addr_test1xpg7q28yhe54fns6c6ejw68sgvpalagftp4l55p2yhmehrj3uq5wf0nf2n8p434nya50qscrml6sjkrtlfgz5f0hnw8qwzxrj9",
        "addr_test1xq7srtmfqejhq6q623mm9gzqu9e8aascjt2qtutza45ruceaqxhkjpn9wp5p54rhk2sypctj0mmp3yk5qhck9mtg8e3sddr4m7",
        "addr_test1xpcqdrhvwvg5gv8mge6gjpl48lzsmemyyx0sn372eqqdjtrsq68wcuc3gsc0k3n53yrl2079phnkggvlp8ru4jqqmykq9kxg5x",
        "addr_test1xz5p4lx5qnnu8vez2nj0mnxfvnkv38uwdh2uswe9t4l9ck4grt7dgp88cwejy48ylhxvje8vez0cumw4eqaj2ht7t3dqm26v7d",
        "addr_test1xqax7xw5rwqmlm9cv59v0zn5jwunlas52mdt94jpkt87aye6duvagxuphlktseg2c798fyae8lmpg4k6kttyrvk0a6fsnuuqje",
        "addr_test1xp6p78scsjdsvv250c39874a4pkhys5s0mj9lfy0p95ffan5ru0p3pymqcc4gl3z20atm2rdwfpfqlhyt7jg7ztgjnmqzk3rch",
        "addr_test1xru27vj3ug40zxthfnmkk4p5wc0y6c0dh6symuffd34y2hlc4ue9rc327yvhwn8hdd2rgas7f4s7m04qfhcjjmr2g40s6t0xv0",
        "addr_test1xzqt8mwuxx83h3sjmsekknf0k080a7757zctxzuszmqxpvuqk0kacvv0r0rp9hpnddxjlv7wlmaafu9skv9eq9kqvzesyrurcg"
    );

    addresses.forEach(b32Address -> {
      ShelleyAddress shelleyAddress = new ShelleyAddress(b32Address);
      Assertions.assertTrue(shelleyAddress.addressHasScript());
    });
  }
}
