package RMI;

import java.io.Serializable;

/**
 * @author Hu Yuxi
 * @date 2018-12-18
 * @see java.io.Serializable
 */
public class User implements Serializable {
    private String name;
    private String password;

    /**
     * 
     * @param name name
     * @param password password
     */
    public User(String name, String password) {
        super();
        this.name = name;
        this.password = password;
    }

    /**
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;

        User user = (User) obj;

        if (!name.equals(user.name)) return false;
        return password != null ? password.equals(user.password) : user.password == null;
    }

    @Override
    public String toString() {
        return "[name: " + name + ",password:" + password + "]";
    }
}
