package com.retailer.invoice.web.rest;

import com.retailer.invoice.domain.Categories;
import com.retailer.invoice.repository.CategoriesRepository;
import com.retailer.invoice.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.retailer.invoice.domain.Categories}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CategoriesResource {

    private final Logger log = LoggerFactory.getLogger(CategoriesResource.class);

    private static final String ENTITY_NAME = "categories";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoriesRepository categoriesRepository;

    public CategoriesResource(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    /**
     * {@code POST  /categories} : Create a new categories.
     *
     * @param categories the categories to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categories, or with status {@code 400 (Bad Request)} if the categories has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/categories")
    public ResponseEntity<Categories> createCategories(@RequestBody Categories categories) throws URISyntaxException {
        log.debug("REST request to save Categories : {}", categories);
        if (categories.getCategoryId() != null) {
            throw new BadRequestAlertException("A new categories cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Categories result = categoriesRepository.save(categories);
        return ResponseEntity
            .created(new URI("/api/categories/" + result.getCategoryId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getCategoryId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /categories/:categoryId} : Updates an existing categories.
     *
     * @param categoryId the id of the categories to save.
     * @param categories the categories to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categories,
     * or with status {@code 400 (Bad Request)} if the categories is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categories couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<Categories> updateCategories(
        @PathVariable(value = "categoryId", required = false) final Long categoryId,
        @RequestBody Categories categories
    ) throws URISyntaxException {
        log.debug("REST request to update Categories : {}, {}", categoryId, categories);
        if (categories.getCategoryId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(categoryId, categories.getCategoryId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoriesRepository.existsById(categoryId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Categories result = categoriesRepository.save(categories);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, categories.getCategoryId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /categories/:categoryId} : Partial updates given fields of an existing categories, field will ignore if it is null
     *
     * @param categoryId the id of the categories to save.
     * @param categories the categories to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categories,
     * or with status {@code 400 (Bad Request)} if the categories is not valid,
     * or with status {@code 404 (Not Found)} if the categories is not found,
     * or with status {@code 500 (Internal Server Error)} if the categories couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/categories/{categoryId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Categories> partialUpdateCategories(
        @PathVariable(value = "categoryId", required = false) final Long categoryId,
        @RequestBody Categories categories
    ) throws URISyntaxException {
        log.debug("REST request to partial update Categories partially : {}, {}", categoryId, categories);
        if (categories.getCategoryId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(categoryId, categories.getCategoryId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoriesRepository.existsById(categoryId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Categories> result = categoriesRepository
            .findById(categories.getCategoryId())
            .map(existingCategories -> {
                if (categories.getCategoryName() != null) {
                    existingCategories.setCategoryName(categories.getCategoryName());
                }
                if (categories.getDescription() != null) {
                    existingCategories.setDescription(categories.getDescription());
                }
                if (categories.getCreatedOn() != null) {
                    existingCategories.setCreatedOn(categories.getCreatedOn());
                }
                if (categories.getUpdatedOn() != null) {
                    existingCategories.setUpdatedOn(categories.getUpdatedOn());
                }
                if (categories.getCreatedBy() != null) {
                    existingCategories.setCreatedBy(categories.getCreatedBy());
                }
                if (categories.getUpdatedBy() != null) {
                    existingCategories.setUpdatedBy(categories.getUpdatedBy());
                }

                return existingCategories;
            })
            .map(categoriesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, categories.getCategoryId().toString())
        );
    }

    /**
     * {@code GET  /categories} : get all the categories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categories in body.
     */
    @GetMapping("/categories")
    public List<Categories> getAllCategories() {
        log.debug("REST request to get all Categories");
        return categoriesRepository.findAll();
    }

    /**
     * {@code GET  /categories/:id} : get the "id" categories.
     *
     * @param id the id of the categories to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categories, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/categories/{id}")
    public ResponseEntity<Categories> getCategories(@PathVariable Long id) {
        log.debug("REST request to get Categories : {}", id);
        Optional<Categories> categories = categoriesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(categories);
    }

    /**
     * {@code DELETE  /categories/:id} : delete the "id" categories.
     *
     * @param id the id of the categories to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategories(@PathVariable Long id) {
        log.debug("REST request to delete Categories : {}", id);
        categoriesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
