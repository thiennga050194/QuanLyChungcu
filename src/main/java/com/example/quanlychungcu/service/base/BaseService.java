package com.example.quanlychungcu.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.lang.NonNull;

public abstract class BaseService<T, ID> {

    @NonNull
    protected abstract JpaRepository<T, ID> getRepository();

    @NonNull
    protected abstract String getSearchField();

    public Page<T> getPaged(int page, int size, String search,
            String sortBy, String sortDirection) {
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);
        // Explicit check to satisfy null safety lints
        if (pageable == null) {
            throw new RuntimeException("Phân trang không thể null!");
        }

        if (search == null || search.trim().isEmpty()) {
            return getRepository().findAll(pageable);
        }

        return searchByField(search, pageable);
    }

    protected abstract Page<T> searchByField(String search, @NonNull Pageable pageable);

    @NonNull
    private Pageable createPageable(int page, int size, String sortBy, String sortDirection) {
        if (sortBy == null || sortBy.trim().isEmpty()) {
            return PageRequest.of(page, size);
        }

        Sort sort = sortDirection.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return PageRequest.of(page, size, sort);
    }
}