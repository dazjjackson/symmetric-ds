package org.jumpmind.symmetric.db.mssql;

import org.jumpmind.symmetric.db.ISymmetricDialect;

public class MsSql2016TriggerTemplate extends MsSql2008TriggerTemplate {
    public MsSql2016TriggerTemplate(ISymmetricDialect symmetricDialect) {
        super(symmetricDialect);
    }
    
    @Override
    protected String getCreateTriggerString() {
        return "create or alter trigger";
    }
}
