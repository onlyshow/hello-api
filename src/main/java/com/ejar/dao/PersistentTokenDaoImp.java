package com.ejar.dao;

import com.ejar.model.PersistentLogins;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;

@Repository("persistentTokenRepository")
@Transactional
public class PersistentTokenDaoImp implements PersistentTokenRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        PersistentLogins logins = new PersistentLogins();

        logins.setUsername(token.getUsername());
        logins.setSeries(token.getSeries());
        logins.setToken(token.getTokenValue());
        logins.setLastUsed(token.getDate());
        sessionFactory.getCurrentSession().save(logins);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        Session session = sessionFactory.getCurrentSession();
        PersistentLogins logins = session.get(PersistentLogins.class, series);
        logins.setToken(tokenValue);
        logins.setLastUsed(lastUsed);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        PersistentLogins logins = sessionFactory.getCurrentSession().get(PersistentLogins.class, seriesId);

        if (logins != null) {
            return new PersistentRememberMeToken(logins.getUsername(), logins.getSeries(), logins.getToken(), logins.getLastUsed());
        }

        return null;
    }

    @Override
    public void removeUserTokens(String username) {
        sessionFactory.getCurrentSession()
            .createQuery("delete from PersistentLogins where username = :username")
            .setParameter("username", username)
            .executeUpdate();
    }
}
