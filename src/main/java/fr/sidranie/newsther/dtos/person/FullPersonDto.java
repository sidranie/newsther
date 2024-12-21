package fr.sidranie.newsther.dtos.person;

import java.util.Set;

import fr.sidranie.newsther.dtos.subscription.ShortSubscriptionDto;

public class FullPersonDto {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String givenName;
    private String familyName;
    private Set<ShortSubscriptionDto> subscriptions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public Set<ShortSubscriptionDto> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<ShortSubscriptionDto> subscriptions) {
        this.subscriptions = subscriptions;
    }
}