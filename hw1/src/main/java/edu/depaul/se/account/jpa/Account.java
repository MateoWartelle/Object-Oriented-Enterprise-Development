package edu.depaul.se.account.jpa;

import edu.depaul.se.account.IAccount;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@SuppressWarnings("unused")
@Entity
@Table(name = "ACCOUNTS")
@NamedQuery(name="findAllAccounts", query="select a from Account a")

public class Account implements Serializable, IAccount {
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	@Column(name = "NAME")
    private String name;
	@Column(name = "BALANCE")
    private Float balance;

    public Account(Integer id, String name, Float balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    // Default constructor
    public Account() {
    }

    public Account(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "accounts.Account[id=" + id + ",name=" + name + ", balance=" + balance + "]";
    }

}
