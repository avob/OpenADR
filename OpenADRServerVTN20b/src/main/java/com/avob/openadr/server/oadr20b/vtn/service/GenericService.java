package com.avob.openadr.server.oadr20b.vtn.service;

import org.springframework.data.repository.CrudRepository;

public abstract class GenericService<T> {

	public abstract CrudRepository<T, Long> getDao();

	public T save(T entity) {
		return getDao().save(entity);
	}

	public Iterable<T> save(Iterable<T> entities) {
		return getDao().save(entities);
	}

	public void delete(Long id) {
		getDao().delete(id);
	}

	public void delete(Iterable<T> entities) {
		getDao().delete(entities);
	}

	public T findOne(Long id) {
		return getDao().findOne(id);
	}

	public Iterable<T> findAll() {
		return getDao().findAll();
	}

}
