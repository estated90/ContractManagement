package com.auxime.contract.constants;

public enum ContractsName {

    CAPE_AUXIME("AUXIME - CAPE VIERGE 2022.docx", "CAPE Auxime"),
    CAPE_ENVOLYS("ENVOLYS - CAPE VIERGE 2022.docx", "CAPE Envolys"),
    PERMANENT_CONTRACT_COELIS("COELIS - CDI VIERGE 2022.docx", "CDI Coelis"),
    PORTAGE_CONVENTION_COELIS("COELIS - CONVENTION PORTAGE VIERGE 2022.docx", "Convention de portage Coelis"),
    TEMPORARY_CONTRACT_COELIS("COELIS - CDD VIERGE 2022.docx", "CDD Coelis"),
    COMMERCIAL_CONTRACT_AUXIME("AUXIME - CONTRAT COMMERCIAL COMMUN.docx", "Contrat commercial Auxime"),
    COMMERCIAL_CONTRACT_COELIS("COELIS - CONTRAT COMMERCIAL COMMUN.docx", "Contrat commercial Coelis"),
    COMMERCIAL_CONTRACT_ENVOLYS("ENVOLYS - CONTRAT COMMERCIAL COMMUN.docx", "Contrat commercial Envolys");

    private String fileName;
    private String friendlyFrName;

    // getter method
    public String getFileName() {
        return this.fileName;
    }

    public String getFriendlyFrName() {
        return this.friendlyFrName;
    }

    // enum constructor - cannot be public or protected
    private ContractsName(String fileName, String friendlyFrName) {
        this.fileName = fileName;
        this.friendlyFrName = friendlyFrName;
    }

}
