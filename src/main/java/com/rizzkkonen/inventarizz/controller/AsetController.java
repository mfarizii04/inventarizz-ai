package com.rizzkkonen.inventarizz.controller;

import com.rizzkkonen.inventarizz.config.HibernateUtil;
import com.rizzkkonen.inventarizz.model.AsetIT;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class AsetController {

    public List<AsetIT> getAllAset(String keyword, int offset, int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM AsetIT WHERE kodeAset LIKE :kw OR namaAset LIKE :kw OR kategori LIKE :kw OR lokasi LIKE :kw ORDER BY id DESC";
            Query<AsetIT> query = session.createQuery(hql, AsetIT.class);
            query.setParameter("kw", "%" + keyword + "%");
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.list();
        }
    }

    public long countAset(String keyword) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(a) FROM AsetIT a WHERE a.kodeAset LIKE :kw OR a.namaAset LIKE :kw OR a.kategori LIKE :kw OR a.lokasi LIKE :kw";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("kw", "%" + keyword + "%");
            return query.uniqueResult();
        }
    }

    public boolean saveAset(AsetIT aset) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(aset);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        }
    }

    public boolean updateAset(AsetIT aset) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(aset);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return false;
        }
    }

    public boolean deleteAset(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            AsetIT a = session.get(AsetIT.class, id);
            if (a != null) {
                session.remove(a);
                tx.commit();
                return true;
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
        }
        return false;
    }
}
