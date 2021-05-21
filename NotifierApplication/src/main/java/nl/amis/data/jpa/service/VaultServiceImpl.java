package nl.amis.data.jpa.service;


import org.springframework.stereotype.Component;

@Component("VaultService")
public class VaultServiceImpl implements VaultService {

    private static final String GEHEIM = "GEHEIM";

    public String getSecret(final String key) {

        return GEHEIM;
    }

}
