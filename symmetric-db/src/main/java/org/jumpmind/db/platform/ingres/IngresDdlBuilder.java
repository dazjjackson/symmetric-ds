/**
 * Licensed to JumpMind Inc under one or more contributor
 * license agreements.  See the NOTICE file distributed
 * with this work for additional information regarding
 * copyright ownership.  JumpMind Inc licenses this file
 * to you under the GNU General Public License, version 3.0 (GPLv3)
 * (the "License"); you may not use this file except in compliance
 * with the License.
 *
 * You should have received a copy of the GNU General Public License,
 * version 3.0 (GPLv3) along with this library; if not, see
 * <http://www.gnu.org/licenses/>.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jumpmind.db.platform.ingres;

import java.sql.Types;

import org.jumpmind.db.alter.ColumnDataTypeChange;
import org.jumpmind.db.model.Column;
import org.jumpmind.db.model.ForeignKey;
import org.jumpmind.db.model.Table;
import org.jumpmind.db.platform.AbstractDdlBuilder;
import org.jumpmind.db.platform.DatabaseNamesConstants;

public class IngresDdlBuilder extends AbstractDdlBuilder {
    public IngresDdlBuilder() {
        super(DatabaseNamesConstants.INGRES);
        databaseInfo.setMaxIdentifierLength(256);
        databaseInfo.setRequiresAutoCommitForDdl(false);
        databaseInfo.addNativeTypeMapping(Types.LONGVARCHAR, "LONG VARCHAR", Types.LONGVARCHAR);
        databaseInfo.addNativeTypeMapping(Types.LONGVARBINARY, "LONG BYTE", Types.LONGVARBINARY);
        databaseInfo.addNativeTypeMapping(Types.DOUBLE, "FLOAT", Types.DOUBLE);
        databaseInfo.addNativeTypeMapping(Types.BIT, "BOOLEAN");
        databaseInfo.setNonPKIdentityColumnsSupported(true);
        databaseInfo.setDefaultValueUsedForIdentitySpec(false);
        databaseInfo.setNonBlankCharColumnSpacePadded(true);
        databaseInfo.setBlankCharColumnSpacePadded(true);
        databaseInfo.setCharColumnSpaceTrimmed(false);
        databaseInfo.setEmptyStringNulled(false);
        databaseInfo.setDelimiterToken("");
        databaseInfo.setDelimitedIdentifiersSupported(false);
        delimitedIdentifierModeOn = false;
    }

    public static boolean isUsePseudoSequence() {
        return "true".equalsIgnoreCase(System.getProperty(
                "org.jumpmind.symmetric.ddl.use.table.seq", "false"));
    }

    @Override
    protected void writeColumnAutoIncrementStmt(Table table, Column column, StringBuilder ddl) {
        ddl.append(" GENERATED BY DEFAULT AS IDENTITY ");
        // ddl.append(" GENERATED ALWAYS AS IDENTITY ");
    }

    @Override
    protected void writeExternalForeignKeyDropStmt(Table table, ForeignKey foreignKey, StringBuilder ddl) {
        writeTableAlterStmt(table, ddl);
        ddl.append("DROP CONSTRAINT ");
        printIdentifier(getForeignKeyName(table, foreignKey), ddl);
        ddl.append(" CASCADE ");
        printEndOfStatement(ddl);
    }

    @Override
    protected boolean writeAlterColumnDataTypeToBigInt(ColumnDataTypeChange change, StringBuilder ddl) {
        writeTableAlterStmt(change.getChangedTable(), ddl);
        ddl.append(" ALTER COLUMN ");
        Column column = change.getChangedColumn();
        column.setTypeCode(change.getNewTypeCode());
        printIdentifier(getColumnName(column), ddl);
        ddl.append(" TYPE ");
        ddl.append(getSqlType(column));
        printEndOfStatement(ddl);
        return true;
    }
}
