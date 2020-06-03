package nawrocki.demo.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;
    @Size(max = 100, message = "Name is too long")
    private String name;
    @Size(max = 100, message = "Email is too long")
    private String email;
    @NotNull(message = "Contact type can not be empty")
    @Enumerated(EnumType.STRING)
    private ContactType type;
    @NotNull(message = "Message can not be empty")
    @Size(max = 1000, message = "Message is too long")
    @Size(min = 1, message = "Message is too short")
    @Column(columnDefinition = "TEXT")
    private String content;
    private Date date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ContactType getType() {
        return type;
    }

    public void setType(ContactType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
