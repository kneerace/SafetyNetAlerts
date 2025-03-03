package com.openclassrooms.SafetyNetAlerts.service;

import com.openclassrooms.SafetyNetAlerts.model.DataLoaded;
import org.springframework.stereotype.Service;

    public interface DataLoaderService {
        DataLoaded loadData();  // loadData method returning DataLoaded object
    }

