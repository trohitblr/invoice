package com.retailer.invoice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.retailer.invoice.IntegrationTest;
import com.retailer.invoice.domain.RetailInventory;
import com.retailer.invoice.repository.RetailInventoryRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RetailInventoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RetailInventoryResourceIT {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Integer DEFAULT_AVAILABLE_QTY = 1;
    private static final Integer UPDATED_AVAILABLE_QTY = 2;

    private static final Integer DEFAULT_SOLD_QTY = 1;
    private static final Integer UPDATED_SOLD_QTY = 2;

    private static final Integer DEFAULT_MAX_LIMIT = 1;
    private static final Integer UPDATED_MAX_LIMIT = 2;

    private static final Integer DEFAULT_MIN_LIMIT = 1;
    private static final Integer UPDATED_MIN_LIMIT = 2;

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/retail-inventories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{retailInventoryId}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RetailInventoryRepository retailInventoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRetailInventoryMockMvc;

    private RetailInventory retailInventory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RetailInventory createEntity(EntityManager em) {
        RetailInventory retailInventory = new RetailInventory()
            .quantity(DEFAULT_QUANTITY)
            .availableQty(DEFAULT_AVAILABLE_QTY)
            .soldQty(DEFAULT_SOLD_QTY)
            .maxLimit(DEFAULT_MAX_LIMIT)
            .minLimit(DEFAULT_MIN_LIMIT)
            .status(DEFAULT_STATUS)
            .createdOn(DEFAULT_CREATED_ON)
            .updatedOn(DEFAULT_UPDATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return retailInventory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RetailInventory createUpdatedEntity(EntityManager em) {
        RetailInventory retailInventory = new RetailInventory()
            .quantity(UPDATED_QUANTITY)
            .availableQty(UPDATED_AVAILABLE_QTY)
            .soldQty(UPDATED_SOLD_QTY)
            .maxLimit(UPDATED_MAX_LIMIT)
            .minLimit(UPDATED_MIN_LIMIT)
            .status(UPDATED_STATUS)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return retailInventory;
    }

    @BeforeEach
    public void initTest() {
        retailInventory = createEntity(em);
    }

    @Test
    @Transactional
    void createRetailInventory() throws Exception {
        int databaseSizeBeforeCreate = retailInventoryRepository.findAll().size();
        // Create the RetailInventory
        restRetailInventoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retailInventory))
            )
            .andExpect(status().isCreated());

        // Validate the RetailInventory in the database
        List<RetailInventory> retailInventoryList = retailInventoryRepository.findAll();
        assertThat(retailInventoryList).hasSize(databaseSizeBeforeCreate + 1);
        RetailInventory testRetailInventory = retailInventoryList.get(retailInventoryList.size() - 1);
        assertThat(testRetailInventory.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testRetailInventory.getAvailableQty()).isEqualTo(DEFAULT_AVAILABLE_QTY);
        assertThat(testRetailInventory.getSoldQty()).isEqualTo(DEFAULT_SOLD_QTY);
        assertThat(testRetailInventory.getMaxLimit()).isEqualTo(DEFAULT_MAX_LIMIT);
        assertThat(testRetailInventory.getMinLimit()).isEqualTo(DEFAULT_MIN_LIMIT);
        assertThat(testRetailInventory.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRetailInventory.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testRetailInventory.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testRetailInventory.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testRetailInventory.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createRetailInventoryWithExistingId() throws Exception {
        // Create the RetailInventory with an existing ID
        retailInventory.setRetailInventoryId(1L);

        int databaseSizeBeforeCreate = retailInventoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRetailInventoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retailInventory))
            )
            .andExpect(status().isBadRequest());

        // Validate the RetailInventory in the database
        List<RetailInventory> retailInventoryList = retailInventoryRepository.findAll();
        assertThat(retailInventoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRetailInventories() throws Exception {
        // Initialize the database
        retailInventoryRepository.saveAndFlush(retailInventory);

        // Get all the retailInventoryList
        restRetailInventoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=retailInventoryId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].retailInventoryId").value(hasItem(retailInventory.getRetailInventoryId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].availableQty").value(hasItem(DEFAULT_AVAILABLE_QTY)))
            .andExpect(jsonPath("$.[*].soldQty").value(hasItem(DEFAULT_SOLD_QTY)))
            .andExpect(jsonPath("$.[*].maxLimit").value(hasItem(DEFAULT_MAX_LIMIT)))
            .andExpect(jsonPath("$.[*].minLimit").value(hasItem(DEFAULT_MIN_LIMIT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getRetailInventory() throws Exception {
        // Initialize the database
        retailInventoryRepository.saveAndFlush(retailInventory);

        // Get the retailInventory
        restRetailInventoryMockMvc
            .perform(get(ENTITY_API_URL_ID, retailInventory.getRetailInventoryId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.retailInventoryId").value(retailInventory.getRetailInventoryId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.availableQty").value(DEFAULT_AVAILABLE_QTY))
            .andExpect(jsonPath("$.soldQty").value(DEFAULT_SOLD_QTY))
            .andExpect(jsonPath("$.maxLimit").value(DEFAULT_MAX_LIMIT))
            .andExpect(jsonPath("$.minLimit").value(DEFAULT_MIN_LIMIT))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingRetailInventory() throws Exception {
        // Get the retailInventory
        restRetailInventoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRetailInventory() throws Exception {
        // Initialize the database
        retailInventoryRepository.saveAndFlush(retailInventory);

        int databaseSizeBeforeUpdate = retailInventoryRepository.findAll().size();

        // Update the retailInventory
        RetailInventory updatedRetailInventory = retailInventoryRepository.findById(retailInventory.getRetailInventoryId()).get();
        // Disconnect from session so that the updates on updatedRetailInventory are not directly saved in db
        em.detach(updatedRetailInventory);
        updatedRetailInventory
            .quantity(UPDATED_QUANTITY)
            .availableQty(UPDATED_AVAILABLE_QTY)
            .soldQty(UPDATED_SOLD_QTY)
            .maxLimit(UPDATED_MAX_LIMIT)
            .minLimit(UPDATED_MIN_LIMIT)
            .status(UPDATED_STATUS)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restRetailInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRetailInventory.getRetailInventoryId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRetailInventory))
            )
            .andExpect(status().isOk());

        // Validate the RetailInventory in the database
        List<RetailInventory> retailInventoryList = retailInventoryRepository.findAll();
        assertThat(retailInventoryList).hasSize(databaseSizeBeforeUpdate);
        RetailInventory testRetailInventory = retailInventoryList.get(retailInventoryList.size() - 1);
        assertThat(testRetailInventory.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testRetailInventory.getAvailableQty()).isEqualTo(UPDATED_AVAILABLE_QTY);
        assertThat(testRetailInventory.getSoldQty()).isEqualTo(UPDATED_SOLD_QTY);
        assertThat(testRetailInventory.getMaxLimit()).isEqualTo(UPDATED_MAX_LIMIT);
        assertThat(testRetailInventory.getMinLimit()).isEqualTo(UPDATED_MIN_LIMIT);
        assertThat(testRetailInventory.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRetailInventory.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testRetailInventory.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testRetailInventory.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRetailInventory.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingRetailInventory() throws Exception {
        int databaseSizeBeforeUpdate = retailInventoryRepository.findAll().size();
        retailInventory.setRetailInventoryId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRetailInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, retailInventory.getRetailInventoryId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(retailInventory))
            )
            .andExpect(status().isBadRequest());

        // Validate the RetailInventory in the database
        List<RetailInventory> retailInventoryList = retailInventoryRepository.findAll();
        assertThat(retailInventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRetailInventory() throws Exception {
        int databaseSizeBeforeUpdate = retailInventoryRepository.findAll().size();
        retailInventory.setRetailInventoryId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetailInventoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(retailInventory))
            )
            .andExpect(status().isBadRequest());

        // Validate the RetailInventory in the database
        List<RetailInventory> retailInventoryList = retailInventoryRepository.findAll();
        assertThat(retailInventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRetailInventory() throws Exception {
        int databaseSizeBeforeUpdate = retailInventoryRepository.findAll().size();
        retailInventory.setRetailInventoryId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetailInventoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retailInventory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RetailInventory in the database
        List<RetailInventory> retailInventoryList = retailInventoryRepository.findAll();
        assertThat(retailInventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRetailInventoryWithPatch() throws Exception {
        // Initialize the database
        retailInventoryRepository.saveAndFlush(retailInventory);

        int databaseSizeBeforeUpdate = retailInventoryRepository.findAll().size();

        // Update the retailInventory using partial update
        RetailInventory partialUpdatedRetailInventory = new RetailInventory();
        partialUpdatedRetailInventory.setRetailInventoryId(retailInventory.getRetailInventoryId());

        partialUpdatedRetailInventory.availableQty(UPDATED_AVAILABLE_QTY).minLimit(UPDATED_MIN_LIMIT).updatedOn(UPDATED_UPDATED_ON);

        restRetailInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRetailInventory.getRetailInventoryId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRetailInventory))
            )
            .andExpect(status().isOk());

        // Validate the RetailInventory in the database
        List<RetailInventory> retailInventoryList = retailInventoryRepository.findAll();
        assertThat(retailInventoryList).hasSize(databaseSizeBeforeUpdate);
        RetailInventory testRetailInventory = retailInventoryList.get(retailInventoryList.size() - 1);
        assertThat(testRetailInventory.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testRetailInventory.getAvailableQty()).isEqualTo(UPDATED_AVAILABLE_QTY);
        assertThat(testRetailInventory.getSoldQty()).isEqualTo(DEFAULT_SOLD_QTY);
        assertThat(testRetailInventory.getMaxLimit()).isEqualTo(DEFAULT_MAX_LIMIT);
        assertThat(testRetailInventory.getMinLimit()).isEqualTo(UPDATED_MIN_LIMIT);
        assertThat(testRetailInventory.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRetailInventory.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testRetailInventory.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testRetailInventory.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testRetailInventory.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateRetailInventoryWithPatch() throws Exception {
        // Initialize the database
        retailInventoryRepository.saveAndFlush(retailInventory);

        int databaseSizeBeforeUpdate = retailInventoryRepository.findAll().size();

        // Update the retailInventory using partial update
        RetailInventory partialUpdatedRetailInventory = new RetailInventory();
        partialUpdatedRetailInventory.setRetailInventoryId(retailInventory.getRetailInventoryId());

        partialUpdatedRetailInventory
            .quantity(UPDATED_QUANTITY)
            .availableQty(UPDATED_AVAILABLE_QTY)
            .soldQty(UPDATED_SOLD_QTY)
            .maxLimit(UPDATED_MAX_LIMIT)
            .minLimit(UPDATED_MIN_LIMIT)
            .status(UPDATED_STATUS)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restRetailInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRetailInventory.getRetailInventoryId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRetailInventory))
            )
            .andExpect(status().isOk());

        // Validate the RetailInventory in the database
        List<RetailInventory> retailInventoryList = retailInventoryRepository.findAll();
        assertThat(retailInventoryList).hasSize(databaseSizeBeforeUpdate);
        RetailInventory testRetailInventory = retailInventoryList.get(retailInventoryList.size() - 1);
        assertThat(testRetailInventory.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testRetailInventory.getAvailableQty()).isEqualTo(UPDATED_AVAILABLE_QTY);
        assertThat(testRetailInventory.getSoldQty()).isEqualTo(UPDATED_SOLD_QTY);
        assertThat(testRetailInventory.getMaxLimit()).isEqualTo(UPDATED_MAX_LIMIT);
        assertThat(testRetailInventory.getMinLimit()).isEqualTo(UPDATED_MIN_LIMIT);
        assertThat(testRetailInventory.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRetailInventory.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testRetailInventory.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testRetailInventory.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRetailInventory.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingRetailInventory() throws Exception {
        int databaseSizeBeforeUpdate = retailInventoryRepository.findAll().size();
        retailInventory.setRetailInventoryId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRetailInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, retailInventory.getRetailInventoryId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(retailInventory))
            )
            .andExpect(status().isBadRequest());

        // Validate the RetailInventory in the database
        List<RetailInventory> retailInventoryList = retailInventoryRepository.findAll();
        assertThat(retailInventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRetailInventory() throws Exception {
        int databaseSizeBeforeUpdate = retailInventoryRepository.findAll().size();
        retailInventory.setRetailInventoryId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetailInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(retailInventory))
            )
            .andExpect(status().isBadRequest());

        // Validate the RetailInventory in the database
        List<RetailInventory> retailInventoryList = retailInventoryRepository.findAll();
        assertThat(retailInventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRetailInventory() throws Exception {
        int databaseSizeBeforeUpdate = retailInventoryRepository.findAll().size();
        retailInventory.setRetailInventoryId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetailInventoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(retailInventory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RetailInventory in the database
        List<RetailInventory> retailInventoryList = retailInventoryRepository.findAll();
        assertThat(retailInventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRetailInventory() throws Exception {
        // Initialize the database
        retailInventoryRepository.saveAndFlush(retailInventory);

        int databaseSizeBeforeDelete = retailInventoryRepository.findAll().size();

        // Delete the retailInventory
        restRetailInventoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, retailInventory.getRetailInventoryId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RetailInventory> retailInventoryList = retailInventoryRepository.findAll();
        assertThat(retailInventoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
