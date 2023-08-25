package org.cardanofoundation.ledgersync.common.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ByronSlotLeaderTest {




  @Test
  void block_1_testnet(){
    var expectedHash = "853b49c9ab5fc52de2a775501ceb639b284057d106e404ecf52cce32";
    var pubKey = "6cef56d6af8845f317949bcdff92358f1394fe11b45c02b28e1d5854c0fa13f8e079644dfc5041afb0fcbd1f47ec7ce064f37b7ec5c070c784a5619ebae2aa60";
    Assertions.assertEquals(expectedHash, SlotLeaderUtils.getByronSlotLeader(pubKey));
  }

  @Test
  void block_488_testnet(){
    var expectedHash = "42186a6a0079ef39ec0414b69d06ae3526e50b4c4c25d043d357abc8";
    var pubKey = "cb51d29ab94e50d9a144d4f426564cec700dee4d9e857aacf91d3b689374d81f742a452818cf2489c16dfc186f6e9c76b7df40845b7c450785f02d8809767575";
    Assertions.assertEquals(expectedHash, SlotLeaderUtils.getByronSlotLeader(pubKey));
  }

  @Test
  void block_26402_testnet(){
    var expectedHash = "42186a6a0079ef39ec0414b69d06ae3526e50b4c4c25d043d357abc8";
    var pubKey = "cb51d29ab94e50d9a144d4f426564cec700dee4d9e857aacf91d3b689374d81f742a452818cf2489c16dfc186f6e9c76b7df40845b7c450785f02d8809767575";
    Assertions.assertEquals(expectedHash, SlotLeaderUtils.getByronSlotLeader(pubKey));
  }

  @Test
  void block_14488_testnet(){
    var expectedHash = "0df4205606dcb8adf868cb937e4792a4826eb849193755aa29574af9";
    var pubKey = "dbbe961151576dadfac3bb579ec2b1c147c00aa4cee03c7ebef495a0ea382940aa0603da8699da09c6bdca320fcd68550ba8b2ac7919e7bd33ed58c5d9dacd44";
    Assertions.assertEquals(expectedHash, SlotLeaderUtils.getByronSlotLeader(pubKey));
  }

  @Test
  void block_16012_testnet(){
    var expectedHash = "a08d73c9dd7f34ae2d68e7631cd80338bc2e88b0c64ed8df52099c91";
    var pubKey = "1c84905901d3cb521d5c433bb13a590cd2103b46a5148fbe60513ab000f994dd41343cd30d2cc37d479aa0055606f4ff2f92dc1dd473b234609cf70e3a6dbc82";
    Assertions.assertEquals(expectedHash, SlotLeaderUtils.getByronSlotLeader(pubKey));
  }

  @Test
  void block_25693_testnet(){
    var expectedHash = "44e51b81adce8430a63c985594cc82b8908e3ab96a4d6b2f33259829";
    var pubKey = "9f0f9fc3d7f76e2522059552e87e06daf940b4581a179aec77f3905662a41865afda5afcdb44157a615cec6ff19ad399d9b0328dc82bc7f3513d40910b7f934a";
    Assertions.assertEquals(expectedHash, SlotLeaderUtils.getByronSlotLeader(pubKey));
  }

  @Test
  void block_25724_testnet(){
    var expectedHash = "d6ff29d80d4007f5d7b883d34e4b73b04bc128fbe15a69eb75800934";
    var pubKey = "53a257951dbbc52b486d63a3a5fcea555cb9ae9ef72dad62e739ea36c23de3ec58a4ecc69a5395622605f3c379399f36c0306d975501f40f9b59735ad2809aa6";
    Assertions.assertEquals(expectedHash, SlotLeaderUtils.getByronSlotLeader(pubKey));
  }

  @Test
  void block_25698_testnet(){
    var expectedHash = "2188aa3235aa3912c6ca9c26a1ada76b5aface7efa2b62078a598e23";
    var pubKey = "29e75f1d7a76480f1a1130449ab5d9a0e014c098850ca15203782adeb7561a81bb3227741749125b32b70ba63c40322f4676a8d657d9a579a0861b7c2f4c0272";
    Assertions.assertEquals(expectedHash, SlotLeaderUtils.getByronSlotLeader(pubKey));
  }

  @Test
  void block_1597133_testnet(){
    var expectedHash = "6df3e1b4b8a84c63c805076a85e5aa00924997a4eae85fddf0aee3ca";
    var issuer = "32a954b521c0b19514408965831ef6839637de7a1a6168bcf8455c504ba93b9c";

    Assertions.assertEquals(expectedHash, SlotLeaderUtils.getAfterByronSlotLeader(issuer));
  }
}
