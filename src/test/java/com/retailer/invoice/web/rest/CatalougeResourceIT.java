package com.retailer.invoice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.retailer.invoice.IntegrationTest;
import com.retailer.invoice.domain.Catalouge;
import com.retailer.invoice.repository.CatalougeRepository;
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
 * Integration tests for the {@link CatalougeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CatalougeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_COST = 1F;
    private static final Float UPDATED_COST = 2F;

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_HSN_NO = 1L;
    private static final Long UPDATED_HSN_NO = 2L;

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/catalouges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{catalougeId}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CatalougeRepository catalougeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCatalougeMockMvc;

    private Catalouge catalouge;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Catalouge createEntity(EntityManager em) {
        Catalouge catalouge = new Catalouge()
            .name(DEFAULT_NAME)
            .cost(DEFAULT_COST)
            .status(DEFAULT_STATUS)
            .description(DEFAULT_DESCRIPTION)
            .hsnNo(DEFAULT_HSN_NO)
            .quantity(DEFAULT_QUANTITY)
            .createdOn(DEFAULT_CREATED_ON)
            .updatedOn(DEFAULT_UPDATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return catalouge;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Catalouge createUpdatedEntity(EntityManager em) {
        Catalouge catalouge = new Catalouge()
            .name(UPDATED_NAME)
            .cost(UPDATED_COST)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .hsnNo(UPDATED_HSN_NO)
            .quantity(UPDATED_QUANTITY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return catalouge;
    }

    @BeforeEach
    public void initTest() {
        catalouge = createEntity(em);
    }

    @Test
    @Transactional
    void createCatalouge() throws Exception {
        int databaseSizeBeforeCreate = catalougeRepository.findAll().size();
        // Create the Catalouge
        restCatalougeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalouge)))
            .andExpect(status().isCreated());

        // Validate the Catalouge in the database
        List<Catalouge> catalougeList = catalougeRepository.findAll();
        assertThat(catalougeList).hasSize(databaseSizeBeforeCreate + 1);
        Catalouge testCatalouge = catalougeList.get(catalougeList.size() - 1);
        assertThat(testCatalouge.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCatalouge.getCost()).isEqualTo(DEFAULT_COST);
        assertThat(testCatalouge.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCatalouge.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCatalouge.getHsnNo()).isEqualTo(DEFAULT_HSN_NO);
        assertThat(testCatalouge.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testCatalouge.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testCatalouge.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testCatalouge.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCatalouge.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createCatalougeWithExistingId() throws Exception {
        // Create the Catalouge with an existing ID
        catalouge.setCatalougeId(1L);

        int databaseSizeBeforeCreate = catalougeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCatalougeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalouge)))
            .andExpect(status().isBadRequest());

        // Validate the Catalouge in the database
        List<Catalouge> catalougeList = catalougeRepository.findAll();
        assertThat(catalougeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCatalouges() throws Exception {
        // Initialize the database
        catalougeRepository.saveAndFlush(catalouge);

        // Get all the catalougeList
        restCatalougeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=catalougeId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].catalougeId").value(hasItem(catalouge.getCatalougeId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].hsnNo").value(hasItem(DEFAULT_HSN_NO.intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getCatalouge() throws Exception {
        // Initialize the database
        catalougeRepository.saveAndFlush(catalouge);

        // Get the catalouge
        restCatalougeMockMvc
            .perform(get(ENTITY_API_URL_ID, catalouge.getCatalougeId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.catalougeId").value(catalouge.getCatalougeId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.hsnNo").value(DEFAULT_HSN_NO.intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingCatalouge() throws Exception {
        // Get the catalouge
        restCatalougeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCatalouge() throws Exception {
        // Initialize the database
        catalougeRepository.saveAndFlush(catalouge);

        int databaseSizeBeforeUpdate = catalougeRepository.findAll().size();

        // Update the catalouge
        Catalouge updatedCatalouge = catalougeRepository.findById(catalouge.getCatalougeId()).get();
        // Disconnect from session so that the updates on updatedCatalouge are not directly saved in db
        em.detach(updatedCatalouge);
        updatedCatalouge
            .name(UPDATED_NAME)
            .cost(UPDATED_COST)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .hsnNo(UPDATED_HSN_NO)
            .quantity(UPDATED_QUANTITY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restCatalougeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCatalouge.getCatalougeId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCatalouge))
            )
            .andExpect(status().isOk());

        // Validate the Catalouge in the database
        List<Catalouge> catalougeList = catalougeRepository.findAll();
        assertThat(catalougeList).hasSize(databaseSizeBeforeUpdate);
        Catalouge testCatalouge = catalougeList.get(catalougeList.size() - 1);
        assertThat(testCatalouge.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCatalouge.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testCatalouge.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCatalouge.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCatalouge.getHsnNo()).isEqualTo(UPDATED_HSN_NO);
        assertThat(testCatalouge.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testCatalouge.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testCatalouge.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testCatalouge.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCatalouge.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingCatalouge() throws Exception {
        int databaseSizeBeforeUpdate = catalougeRepository.findAll().size();
        catalouge.setCatalougeId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalougeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catalouge.getCatalougeId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalouge))
            )
            .andExpect(status().isBadRequest());

        // Validate the Catalouge in the database
        List<Catalouge> catalougeList = catalougeRepository.findAll();
        assertThat(catalougeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCatalouge() throws Exception {
        int databaseSizeBeforeUpdate = catalougeRepository.findAll().size();
        catalouge.setCatalougeId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalougeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalouge))
            )
            .andExpect(status().isBadRequest());

        // Validate the Catalouge in the database
        List<Catalouge> catalougeList = catalougeRepository.findAll();
        assertThat(catalougeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCatalouge() throws Exception {
        int databaseSizeBeforeUpdate = catalougeRepository.findAll().size();
        catalouge.setCatalougeId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalougeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(catalouge)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Catalouge in the database
        List<Catalouge> catalougeList = catalougeRepository.findAll();
        assertThat(catalougeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCatalougeWithPatch() throws Exception {
        // Initialize the database
        catalougeRepository.saveAndFlush(catalouge);

        int databaseSizeBeforeUpdate = catalougeRepository.findAll().size();

        // Update the catalouge using partial update
        Catalouge partialUpdatedCatalouge = new Catalouge();
        partialUpdatedCatalouge.setCatalougeId(catalouge.getCatalougeId());

        partialUpdatedCatalouge.createdOn(UPDATED_CREATED_ON).createdBy(UPDATED_CREATED_BY).updatedBy(UPDATED_UPDATED_BY);

        restCatalougeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalouge.getCatalougeId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalouge))
            )
            .andExpect(status().isOk());

        // Validate the Catalouge in the database
        List<Catalouge> catalougeList = catalougeRepository.findAll();
        assertThat(catalougeList).hasSize(databaseSizeBeforeUpdate);
        Catalouge testCatalouge = catalougeList.get(catalougeList.size() - 1);
        assertThat(testCatalouge.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCatalouge.getCost()).isEqualTo(DEFAULT_COST);
        assertThat(testCatalouge.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCatalouge.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCatalouge.getHsnNo()).isEqualTo(DEFAULT_HSN_NO);
        assertThat(testCatalouge.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testCatalouge.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testCatalouge.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testCatalouge.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCatalouge.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateCatalougeWithPatch() throws Exception {
        // Initialize the database
        catalougeRepository.saveAndFlush(catalouge);

        int databaseSizeBeforeUpdate = catalougeRepository.findAll().size();

        // Update the catalouge using partial update
        Catalouge partialUpdatedCatalouge = new Catalouge();
        partialUpdatedCatalouge.setCatalougeId(catalouge.getCatalougeId());

        partialUpdatedCatalouge
            .name(UPDATED_NAME)
            .cost(UPDATED_COST)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION)
            .hsnNo(UPDATED_HSN_NO)
            .quantity(UPDATED_QUANTITY)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restCatalougeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalouge.getCatalougeId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalouge))
            )
            .andExpect(status().isOk());

        // Validate the Catalouge in the database
        List<Catalouge> catalougeList = catalougeRepository.findAll();
        assertThat(catalougeList).hasSize(databaseSizeBeforeUpdate);
        Catalouge testCatalouge = catalougeList.get(catalougeList.size() - 1);
        assertThat(testCatalouge.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCatalouge.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testCatalouge.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCatalouge.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCatalouge.getHsnNo()).isEqualTo(UPDATED_HSN_NO);
        assertThat(testCatalouge.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testCatalouge.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testCatalouge.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testCatalouge.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCatalouge.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingCatalouge() throws Exception {
        int databaseSizeBeforeUpdate = catalougeRepository.findAll().size();
        catalouge.setCatalougeId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalougeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, catalouge.getCatalougeId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalouge))
            )
            .andExpect(status().isBadRequest());

        // Validate the Catalouge in the database
        List<Catalouge> catalougeList = catalougeRepository.findAll();
        assertThat(catalougeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCatalouge() throws Exception {
        int databaseSizeBeforeUpdate = catalougeRepository.findAll().size();
        catalouge.setCatalougeId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalougeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalouge))
            )
            .andExpect(status().isBadRequest());

        // Validate the Catalouge in the database
        List<Catalouge> catalougeList = catalougeRepository.findAll();
        assertThat(catalougeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCatalouge() throws Exception {
        int databaseSizeBeforeUpdate = catalougeRepository.findAll().size();
        catalouge.setCatalougeId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalougeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(catalouge))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Catalouge in the database
        List<Catalouge> catalougeList = catalougeRepository.findAll();
        assertThat(catalougeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCatalouge() throws Exception {
        // Initialize the database
        catalougeRepository.saveAndFlush(catalouge);

        int databaseSizeBeforeDelete = catalougeRepository.findAll().size();

        // Delete the catalouge
        restCatalougeMockMvc
            .perform(delete(ENTITY_API_URL_ID, catalouge.getCatalougeId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Catalouge> catalougeList = catalougeRepository.findAll();
        assertThat(catalougeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
