package org.cardanofoundation.ledgersync.common.common.mapper;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cardanofoundation.ledgersync.common.common.certs.StakeCredential;
import java.io.IOException;

public class StakeCredentialDeserializer extends KeyDeserializer {


  @Override
  public Object deserializeKey(String s, DeserializationContext deserializationContext)
      throws IOException {
    return new ObjectMapper().readValue(s, StakeCredential.class);
  }
}
