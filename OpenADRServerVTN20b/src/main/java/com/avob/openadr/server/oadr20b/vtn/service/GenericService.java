package com.avob.openadr.server.oadr20b.vtn.service;

import org.springframework.data.repository.CrudRepository;

public abstract class GenericService<T> {

	public abstract CrudRepository<T, Long> getDao();

	public T save(T entity) {
		return getDao().save(entity);
	}

	public Iterable<T> save(Iterable<T> entities) {
		return getDao().saveAll(entities);
	}

	public void delete(Long id) {
		getDao().deleteById(id);
	}

	public void delete(Iterable<T> entities) {
		getDao().deleteAll(entities);
	}

	public T findOne(Long id) {
		return getDao().findById(id).get();
	}

	public Iterable<T> findAll() {
		return getDao().findAll();
	}
	
}
