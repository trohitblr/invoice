package com.retailer.invoice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.retailer.invoice.IntegrationTest;
import com.retailer.invoice.domain.Retailer;
import com.retailer.invoice.domain.enumeration.Status;
import com.retailer.invoice.repository.RetailerRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RetailerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RetailerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_OWNER = "BBBBBBBBBB";

    private static final Integer DEFAULT_PHONE = 10;
    private static final Integer UPDATED_PHONE = 11;

    private static final String DEFAULT_EMAIL = "9I@\\SSSS";
    private static final String UPDATED_EMAIL = "Eb\\@\\SSSSSS";

    private static final String DEFAULT_GST_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_GST_NUMBER = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.ACTIVE;
    private static final Status UPDATED_STATUS = Status.INACTIVE;

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/retailers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{retailerId}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RetailerRepository retailerRepository;

    @Mock
    private RetailerRepository retailerRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRetailerMockMvc;

    private Retailer retailer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Retailer createEntity(EntityManager em) {
        Retailer retailer = new Retailer()
            .name(DEFAULT_NAME)
            .owner(DEFAULT_OWNER)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL)
            .gstNumber(DEFAULT_GST_NUMBER)
            .status(DEFAULT_STATUS)
            .createdOn(DEFAULT_CREATED_ON)
            .updatedOn(DEFAULT_UPDATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return retailer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Retailer createUpdatedEntity(EntityManager em) {
        Retailer retailer = new Retailer()
            .name(UPDATED_NAME)
            .owner(UPDATED_OWNER)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .gstNumber(UPDATED_GST_NUMBER)
            .status(UPDATED_STATUS)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return retailer;
    }

    @BeforeEach
    public void initTest() {
        retailer = createEntity(em);
    }

    @Test
    @Transactional
    void createRetailer() throws Exception {
        int databaseSizeBeforeCreate = retailerRepository.findAll().size();
        // Create the Retailer
        restRetailerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retailer)))
            .andExpect(status().isCreated());

        // Validate the Retailer in the database
        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeCreate + 1);
        Retailer testRetailer = retailerList.get(retailerList.size() - 1);
        assertThat(testRetailer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRetailer.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testRetailer.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testRetailer.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testRetailer.getGstNumber()).isEqualTo(DEFAULT_GST_NUMBER);
        assertThat(testRetailer.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRetailer.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testRetailer.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testRetailer.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testRetailer.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createRetailerWithExistingId() throws Exception {
        // Create the Retailer with an existing ID
        retailer.setRetailerId(1L);

        int databaseSizeBeforeCreate = retailerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRetailerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retailer)))
            .andExpect(status().isBadRequest());

        // Validate the Retailer in the database
        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = retailerRepository.findAll().size();
        // set the field null
        retailer.setName(null);

        // Create the Retailer, which fails.

        restRetailerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retailer)))
            .andExpect(status().isBadRequest());

        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRetailers() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get all the retailerList
        restRetailerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=retailerId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].retailerId").value(hasItem(retailer.getRetailerId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].gstNumber").value(hasItem(DEFAULT_GST_NUMBER)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRetailersWithEagerRelationshipsIsEnabled() throws Exception {
        when(retailerRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRetailerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(retailerRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRetailersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(retailerRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRetailerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(retailerRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRetailer() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        // Get the retailer
        restRetailerMockMvc
            .perform(get(ENTITY_API_URL_ID, retailer.getRetailerId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.retailerId").value(retailer.getRetailerId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.gstNumber").value(DEFAULT_GST_NUMBER))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingRetailer() throws Exception {
        // Get the retailer
        restRetailerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRetailer() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        int databaseSizeBeforeUpdate = retailerRepository.findAll().size();

        // Update the retailer
        Retailer updatedRetailer = retailerRepository.findById(retailer.getRetailerId()).get();
        // Disconnect from session so that the updates on updatedRetailer are not directly saved in db
        em.detach(updatedRetailer);
        updatedRetailer
            .name(UPDATED_NAME)
            .owner(UPDATED_OWNER)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .gstNumber(UPDATED_GST_NUMBER)
            .status(UPDATED_STATUS)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restRetailerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRetailer.getRetailerId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRetailer))
            )
            .andExpect(status().isOk());

        // Validate the Retailer in the database
        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeUpdate);
        Retailer testRetailer = retailerList.get(retailerList.size() - 1);
        assertThat(testRetailer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRetailer.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testRetailer.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testRetailer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testRetailer.getGstNumber()).isEqualTo(UPDATED_GST_NUMBER);
        assertThat(testRetailer.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRetailer.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testRetailer.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testRetailer.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRetailer.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingRetailer() throws Exception {
        int databaseSizeBeforeUpdate = retailerRepository.findAll().size();
        retailer.setRetailerId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRetailerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, retailer.getRetailerId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(retailer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Retailer in the database
        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRetailer() throws Exception {
        int databaseSizeBeforeUpdate = retailerRepository.findAll().size();
        retailer.setRetailerId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetailerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(retailer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Retailer in the database
        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRetailer() throws Exception {
        int databaseSizeBeforeUpdate = retailerRepository.findAll().size();
        retailer.setRetailerId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetailerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retailer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Retailer in the database
        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRetailerWithPatch() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        int databaseSizeBeforeUpdate = retailerRepository.findAll().size();

        // Update the retailer using partial update
        Retailer partialUpdatedRetailer = new Retailer();
        partialUpdatedRetailer.setRetailerId(retailer.getRetailerId());

        partialUpdatedRetailer
            .name(UPDATED_NAME)
            .owner(UPDATED_OWNER)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .updatedBy(UPDATED_UPDATED_BY);

        restRetailerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRetailer.getRetailerId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRetailer))
            )
            .andExpect(status().isOk());

        // Validate the Retailer in the database
        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeUpdate);
        Retailer testRetailer = retailerList.get(retailerList.size() - 1);
        assertThat(testRetailer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRetailer.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testRetailer.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testRetailer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testRetailer.getGstNumber()).isEqualTo(DEFAULT_GST_NUMBER);
        assertThat(testRetailer.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRetailer.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testRetailer.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testRetailer.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testRetailer.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateRetailerWithPatch() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        int databaseSizeBeforeUpdate = retailerRepository.findAll().size();

        // Update the retailer using partial update
        Retailer partialUpdatedRetailer = new Retailer();
        partialUpdatedRetailer.setRetailerId(retailer.getRetailerId());

        partialUpdatedRetailer
            .name(UPDATED_NAME)
            .owner(UPDATED_OWNER)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .gstNumber(UPDATED_GST_NUMBER)
            .status(UPDATED_STATUS)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restRetailerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRetailer.getRetailerId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRetailer))
            )
            .andExpect(status().isOk());

        // Validate the Retailer in the database
        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeUpdate);
        Retailer testRetailer = retailerList.get(retailerList.size() - 1);
        assertThat(testRetailer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRetailer.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testRetailer.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testRetailer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testRetailer.getGstNumber()).isEqualTo(UPDATED_GST_NUMBER);
        assertThat(testRetailer.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRetailer.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testRetailer.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testRetailer.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRetailer.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingRetailer() throws Exception {
        int databaseSizeBeforeUpdate = retailerRepository.findAll().size();
        retailer.setRetailerId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRetailerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, retailer.getRetailerId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(retailer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Retailer in the database
        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRetailer() throws Exception {
        int databaseSizeBeforeUpdate = retailerRepository.findAll().size();
        retailer.setRetailerId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetailerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(retailer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Retailer in the database
        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRetailer() throws Exception {
        int databaseSizeBeforeUpdate = retailerRepository.findAll().size();
        retailer.setRetailerId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetailerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(retailer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Retailer in the database
        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRetailer() throws Exception {
        // Initialize the database
        retailerRepository.saveAndFlush(retailer);

        int databaseSizeBeforeDelete = retailerRepository.findAll().size();

        // Delete the retailer
        restRetailerMockMvc
            .perform(delete(ENTITY_API_URL_ID, retailer.getRetailerId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Retailer> retailerList = retailerRepository.findAll();
        assertThat(retailerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
