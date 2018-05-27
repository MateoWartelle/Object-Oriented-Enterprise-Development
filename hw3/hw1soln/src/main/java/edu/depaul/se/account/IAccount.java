package edu.depaul.se.account;

public interface IAccount {

    Float getBalance();

    Integer getId();

    String getName();

    void setBalance(Float balance);

    void setId(Integer id);

    void setName(String name);

}
