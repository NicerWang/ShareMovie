package nk.bigdata.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    String tel;
    String name;
    String pwd;
    boolean su = false;

    public User(String tel, String name, String pwd) {
        this.tel = tel;
        this.name = name;
        this.pwd = pwd;
    }
}
