package payments.domains;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "accounts",
        uniqueConstraints = @UniqueConstraint(name = "uk_accounts_user_id", columnNames = "user_id")
)
@Getter
@Setter
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "balance", nullable = false)
    private Long balance = 0L;

    public Account(Integer userId) {
        this.userId = userId;
        this.balance = 0L;
    }

    public Long getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public Long getBalance() {
        return balance;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

}
