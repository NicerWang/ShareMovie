package nk.bigdata.backend.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Config implements Serializable {
    private static final long serialVersionUID = 1;
    public String dataBaseAddr;
    public String username;
    public String password;
    public String tableName;
    public String alternativeHosts;
}
