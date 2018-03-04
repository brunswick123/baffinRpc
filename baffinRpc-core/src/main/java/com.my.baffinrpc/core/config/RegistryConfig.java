package com.my.baffinrpc.core.config;

import com.my.baffinrpc.core.registry.RegistryService;

public class RegistryConfig {

    private RegistryService registryService;

    public RegistryService getRegistryService() {
        return registryService;
    }

    public void setRegistryService(RegistryService registryService) {
        this.registryService = registryService;
    }
}
