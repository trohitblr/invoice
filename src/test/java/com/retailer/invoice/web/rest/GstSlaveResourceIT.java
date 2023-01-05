package com.retailer.invoice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.retailer.invoice.IntegrationTest;
import com.retailer.invoice.domain.GstSlave;
import com.retailer.invoice.domain.enumeration.GstType;
import com.retailer.invoice.repository.GstSlaveRepository;
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
 * Integration tests for the {@link GstSlaveResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GstSlaveResourceIT {

    private static final GstType DEFAULT_TAX = GstType.CGSTSGST;
    private static final GstType UPDATED_TAX = GstType.OTHER;

    private static final Float DEFAULT_TAX_PERCENTAGE = 1F;
    private static final Float UPDATED_TAX_PERCENTAGE = 2F;

    private static final String ENTITY_API_URL = "/api/gst-slaves";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{gstSlaveId}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GstSlaveRepository gstSlaveRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGstSlaveMockMvc;

    private GstSlave gstSlave;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GstSlave createEntity(EntityManager em) {
        GstSlave gstSlave = new GstSlave().tax(DEFAULT_TAX).taxPercentage(DEFAULT_TAX_PERCENTAGE);
        return gstSlave;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GstSlave createUpdatedEntity(EntityManager em) {
        GstSlave gstSlave = new GstSlave().tax(UPDATED_TAX).taxPercentage(UPDATED_TAX_PERCENTAGE);
        return gstSlave;
    }

    @BeforeEach
    public void initTest() {
        gstSlave = createEntity(em);
    }

    @Test
    @Transactional
    void createGstSlave() throws Exception {
        int databaseSizeBeforeCreate = gstSlaveRepository.findAll().size();
        // Create the GstSlave
        restGstSlaveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gstSlave)))
            .andExpect(status().isCreated());

        // Validate the GstSlave in the database
        List<GstSlave> gstSlaveList = gstSlaveRepository.findAll();
        assertThat(gstSlaveList).hasSize(databaseSizeBeforeCreate + 1);
        GstSlave testGstSlave = gstSlaveList.get(gstSlaveList.size() - 1);
        assertThat(testGstSlave.getTax()).isEqualTo(DEFAULT_TAX);
        assertThat(testGstSlave.getTaxPercentage()).isEqualTo(DEFAULT_TAX_PERCENTAGE);
    }

    @Test
    @Transactional
    void createGstSlaveWithExistingId() throws Exception {
        // Create the GstSlave with an existing ID
        gstSlave.setGstSlaveId(1L);

        int databaseSizeBeforeCreate = gstSlaveRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGstSlaveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gstSlave)))
            .andExpect(status().isBadRequest());

        // Validate the GstSlave in the database
        List<GstSlave> gstSlaveList = gstSlaveRepository.findAll();
        assertThat(gstSlaveList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGstSlaves() throws Exception {
        // Initialize the database
        gstSlaveRepository.saveAndFlush(gstSlave);

        // Get all the gstSlaveList
        restGstSlaveMockMvc
            .perform(get(ENTITY_API_URL + "?sort=gstSlaveId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].gstSlaveId").value(hasItem(gstSlave.getGstSlaveId().intValue())))
            .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX.toString())))
            .andExpect(jsonPath("$.[*].taxPercentage").value(hasItem(DEFAULT_TAX_PERCENTAGE.doubleValue())));
    }

    @Test
    @Transactional
    void getGstSlave() throws Exception {
        // Initialize the database
        gstSlaveRepository.saveAndFlush(gstSlave);

        // Get the gstSlave
        restGstSlaveMockMvc
            .perform(get(ENTITY_API_URL_ID, gstSlave.getGstSlaveId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.gstSlaveId").value(gstSlave.getGstSlaveId().intValue()))
            .andExpect(jsonPath("$.tax").value(DEFAULT_TAX.toString()))
            .andExpect(jsonPath("$.taxPercentage").value(DEFAULT_TAX_PERCENTAGE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingGstSlave() throws Exception {
        // Get the gstSlave
        restGstSlaveMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGstSlave() throws Exception {
        // Initialize the database
        gstSlaveRepository.saveAndFlush(gstSlave);

        int databaseSizeBeforeUpdate = gstSlaveRepository.findAll().size();

        // Update the gstSlave
        GstSlave updatedGstSlave = gstSlaveRepository.findById(gstSlave.getGstSlaveId()).get();
        // Disconnect from session so that the updates on updatedGstSlave are not directly saved in db
        em.detach(updatedGstSlave);
        updatedGstSlave.tax(UPDATED_TAX).taxPercentage(UPDATED_TAX_PERCENTAGE);

        restGstSlaveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGstSlave.getGstSlaveId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGstSlave))
            )
            .andExpect(status().isOk());

        // Validate the GstSlave in the database
        List<GstSlave> gstSlaveList = gstSlaveRepository.findAll();
        assertThat(gstSlaveList).hasSize(databaseSizeBeforeUpdate);
        GstSlave testGstSlave = gstSlaveList.get(gstSlaveList.size() - 1);
        assertThat(testGstSlave.getTax()).isEqualTo(UPDATED_TAX);
        assertThat(testGstSlave.getTaxPercentage()).isEqualTo(UPDATED_TAX_PERCENTAGE);
    }

    @Test
    @Transactional
    void putNonExistingGstSlave() throws Exception {
        int databaseSizeBeforeUpdate = gstSlaveRepository.findAll().size();
        gstSlave.setGstSlaveId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGstSlaveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gstSlave.getGstSlaveId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gstSlave))
            )
            .andExpect(status().isBadRequest());

        // Validate the GstSlave in the database
        List<GstSlave> gstSlaveList = gstSlaveRepository.findAll();
        assertThat(gstSlaveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGstSlave() throws Exception {
        int databaseSizeBeforeUpdate = gstSlaveRepository.findAll().size();
        gstSlave.setGstSlaveId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGstSlaveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gstSlave))
            )
            .andExpect(status().isBadRequest());

        // Validate the GstSlave in the database
        List<GstSlave> gstSlaveList = gstSlaveRepository.findAll();
        assertThat(gstSlaveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGstSlave() throws Exception {
        int databaseSizeBeforeUpdate = gstSlaveRepository.findAll().size();
        gstSlave.setGstSlaveId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGstSlaveMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gstSlave)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GstSlave in the database
        List<GstSlave> gstSlaveList = gstSlaveRepository.findAll();
        assertThat(gstSlaveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGstSlaveWithPatch() throws Exception {
        // Initialize the database
        gstSlaveRepository.saveAndFlush(gstSlave);

        int databaseSizeBeforeUpdate = gstSlaveRepository.findAll().size();

        // Update the gstSlave using partial update
        GstSlave partialUpdatedGstSlave = new GstSlave();
        partialUpdatedGstSlave.setGstSlaveId(gstSlave.getGstSlaveId());

        partialUpdatedGstSlave.taxPercentage(UPDATED_TAX_PERCENTAGE);

        restGstSlaveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGstSlave.getGstSlaveId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGstSlave))
            )
            .andExpect(status().isOk());

        // Validate the GstSlave in the database
        List<GstSlave> gstSlaveList = gstSlaveRepository.findAll();
        assertThat(gstSlaveList).hasSize(databaseSizeBeforeUpdate);
        GstSlave testGstSlave = gstSlaveList.get(gstSlaveList.size() - 1);
        assertThat(testGstSlave.getTax()).isEqualTo(DEFAULT_TAX);
        assertThat(testGstSlave.getTaxPercentage()).isEqualTo(UPDATED_TAX_PERCENTAGE);
    }

    @Test
    @Transactional
    void fullUpdateGstSlaveWithPatch() throws Exception {
        // Initialize the database
        gstSlaveRepository.saveAndFlush(gstSlave);

        int databaseSizeBeforeUpdate = gstSlaveRepository.findAll().size();

        // Update the gstSlave using partial update
        GstSlave partialUpdatedGstSlave = new GstSlave();
        partialUpdatedGstSlave.setGstSlaveId(gstSlave.getGstSlaveId());

        partialUpdatedGstSlave.tax(UPDATED_TAX).taxPercentage(UPDATED_TAX_PERCENTAGE);

        restGstSlaveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGstSlave.getGstSlaveId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGstSlave))
            )
            .andExpect(status().isOk());

        // Validate the GstSlave in the database
        List<GstSlave> gstSlaveList = gstSlaveRepository.findAll();
        assertThat(gstSlaveList).hasSize(databaseSizeBeforeUpdate);
        GstSlave testGstSlave = gstSlaveList.get(gstSlaveList.size() - 1);
        assertThat(testGstSlave.getTax()).isEqualTo(UPDATED_TAX);
        assertThat(testGstSlave.getTaxPercentage()).isEqualTo(UPDATED_TAX_PERCENTAGE);
    }

    @Test
    @Transactional
    void patchNonExistingGstSlave() throws Exception {
        int databaseSizeBeforeUpdate = gstSlaveRepository.findAll().size();
        gstSlave.setGstSlaveId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGstSlaveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gstSlave.getGstSlaveId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gstSlave))
            )
            .andExpect(status().isBadRequest());

        // Validate the GstSlave in the database
        List<GstSlave> gstSlaveList = gstSlaveRepository.findAll();
        assertThat(gstSlaveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGstSlave() throws Exception {
        int databaseSizeBeforeUpdate = gstSlaveRepository.findAll().size();
        gstSlave.setGstSlaveId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGstSlaveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gstSlave))
            )
            .andExpect(status().isBadRequest());

        // Validate the GstSlave in the database
        List<GstSlave> gstSlaveList = gstSlaveRepository.findAll();
        assertThat(gstSlaveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGstSlave() throws Exception {
        int databaseSizeBeforeUpdate = gstSlaveRepository.findAll().size();
        gstSlave.setGstSlaveId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGstSlaveMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gstSlave)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GstSlave in the database
        List<GstSlave> gstSlaveList = gstSlaveRepository.findAll();
        assertThat(gstSlaveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGstSlave() throws Exception {
        // Initialize the database
        gstSlaveRepository.saveAndFlush(gstSlave);

        int databaseSizeBeforeDelete = gstSlaveRepository.findAll().size();

        // Delete the gstSlave
        restGstSlaveMockMvc
            .perform(delete(ENTITY_API_URL_ID, gstSlave.getGstSlaveId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GstSlave> gstSlaveList = gstSlaveRepository.findAll();
        assertThat(gstSlaveList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
