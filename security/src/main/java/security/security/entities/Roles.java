package security.security.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_roles")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long role_id;


    private String name;

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long roleId) {
        this.role_id = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public enum Values{
    	admin(1L),

    	basic(2L);

        long roleId;

        Values(long roleId){
            this.roleId = roleId;
        }

        public long getRoleId() {
            return roleId;
        }
    }
}
