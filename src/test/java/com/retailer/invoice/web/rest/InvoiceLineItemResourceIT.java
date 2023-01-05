package com.retailer.invoice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.retailer.invoice.IntegrationTest;
import com.retailer.invoice.domain.InvoiceLineItem;
import com.retailer.invoice.domain.enumeration.ArticleType;
import com.retailer.invoice.repository.InvoiceLineItemRepository;
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
 * Integration tests for the {@link InvoiceLineItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceLineItemResourceIT {

    private static final ArticleType DEFAULT_ARTICLE_TYPE = ArticleType.GNG;
    private static final ArticleType UPDATED_ARTICLE_TYPE = ArticleType.AIR;

    private static final String DEFAULT_ARTICLE_ID = "AAAAAAAAAA";
    private static final String UPDATED_ARTICLE_ID = "BBBBBBBBBB";

    private static final Long DEFAULT_QUANTITY = 1L;
    private static final Long UPDATED_QUANTITY = 2L;

    private static final Integer DEFAULT_AMOUNT = 1;
    private static final Integer UPDATED_AMOUNT = 2;

    private static final Float DEFAULT_DISCOUNT = 1F;
    private static final Float UPDATED_DISCOUNT = 2F;

    private static final Instant DEFAULT_CREATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/invoice-line-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{invoiceLineItemId}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InvoiceLineItemRepository invoiceLineItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceLineItemMockMvc;

    private InvoiceLineItem invoiceLineItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceLineItem createEntity(EntityManager em) {
        InvoiceLineItem invoiceLineItem = new InvoiceLineItem()
            .articleType(DEFAULT_ARTICLE_TYPE)
            .articleId(DEFAULT_ARTICLE_ID)
            .quantity(DEFAULT_QUANTITY)
            .amount(DEFAULT_AMOUNT)
            .discount(DEFAULT_DISCOUNT)
            .createdOn(DEFAULT_CREATED_ON)
            .updatedOn(DEFAULT_UPDATED_ON)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY);
        return invoiceLineItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceLineItem createUpdatedEntity(EntityManager em) {
        InvoiceLineItem invoiceLineItem = new InvoiceLineItem()
            .articleType(UPDATED_ARTICLE_TYPE)
            .articleId(UPDATED_ARTICLE_ID)
            .quantity(UPDATED_QUANTITY)
            .amount(UPDATED_AMOUNT)
            .discount(UPDATED_DISCOUNT)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);
        return invoiceLineItem;
    }

    @BeforeEach
    public void initTest() {
        invoiceLineItem = createEntity(em);
    }

    @Test
    @Transactional
    void createInvoiceLineItem() throws Exception {
        int databaseSizeBeforeCreate = invoiceLineItemRepository.findAll().size();
        // Create the InvoiceLineItem
        restInvoiceLineItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceLineItem))
            )
            .andExpect(status().isCreated());

        // Validate the InvoiceLineItem in the database
        List<InvoiceLineItem> invoiceLineItemList = invoiceLineItemRepository.findAll();
        assertThat(invoiceLineItemList).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceLineItem testInvoiceLineItem = invoiceLineItemList.get(invoiceLineItemList.size() - 1);
        assertThat(testInvoiceLineItem.getArticleType()).isEqualTo(DEFAULT_ARTICLE_TYPE);
        assertThat(testInvoiceLineItem.getArticleId()).isEqualTo(DEFAULT_ARTICLE_ID);
        assertThat(testInvoiceLineItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testInvoiceLineItem.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testInvoiceLineItem.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testInvoiceLineItem.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testInvoiceLineItem.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testInvoiceLineItem.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testInvoiceLineItem.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void createInvoiceLineItemWithExistingId() throws Exception {
        // Create the InvoiceLineItem with an existing ID
        invoiceLineItem.setInvoiceLineItemId(1L);

        int databaseSizeBeforeCreate = invoiceLineItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceLineItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceLineItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceLineItem in the database
        List<InvoiceLineItem> invoiceLineItemList = invoiceLineItemRepository.findAll();
        assertThat(invoiceLineItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInvoiceLineItems() throws Exception {
        // Initialize the database
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem);

        // Get all the invoiceLineItemList
        restInvoiceLineItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=invoiceLineItemId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].invoiceLineItemId").value(hasItem(invoiceLineItem.getInvoiceLineItemId().intValue())))
            .andExpect(jsonPath("$.[*].articleType").value(hasItem(DEFAULT_ARTICLE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].articleId").value(hasItem(DEFAULT_ARTICLE_ID)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)));
    }

    @Test
    @Transactional
    void getInvoiceLineItem() throws Exception {
        // Initialize the database
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem);

        // Get the invoiceLineItem
        restInvoiceLineItemMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceLineItem.getInvoiceLineItemId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.invoiceLineItemId").value(invoiceLineItem.getInvoiceLineItemId().intValue()))
            .andExpect(jsonPath("$.articleType").value(DEFAULT_ARTICLE_TYPE.toString()))
            .andExpect(jsonPath("$.articleId").value(DEFAULT_ARTICLE_ID))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.doubleValue()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingInvoiceLineItem() throws Exception {
        // Get the invoiceLineItem
        restInvoiceLineItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvoiceLineItem() throws Exception {
        // Initialize the database
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem);

        int databaseSizeBeforeUpdate = invoiceLineItemRepository.findAll().size();

        // Update the invoiceLineItem
        InvoiceLineItem updatedInvoiceLineItem = invoiceLineItemRepository.findById(invoiceLineItem.getInvoiceLineItemId()).get();
        // Disconnect from session so that the updates on updatedInvoiceLineItem are not directly saved in db
        em.detach(updatedInvoiceLineItem);
        updatedInvoiceLineItem
            .articleType(UPDATED_ARTICLE_TYPE)
            .articleId(UPDATED_ARTICLE_ID)
            .quantity(UPDATED_QUANTITY)
            .amount(UPDATED_AMOUNT)
            .discount(UPDATED_DISCOUNT)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restInvoiceLineItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInvoiceLineItem.getInvoiceLineItemId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInvoiceLineItem))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceLineItem in the database
        List<InvoiceLineItem> invoiceLineItemList = invoiceLineItemRepository.findAll();
        assertThat(invoiceLineItemList).hasSize(databaseSizeBeforeUpdate);
        InvoiceLineItem testInvoiceLineItem = invoiceLineItemList.get(invoiceLineItemList.size() - 1);
        assertThat(testInvoiceLineItem.getArticleType()).isEqualTo(UPDATED_ARTICLE_TYPE);
        assertThat(testInvoiceLineItem.getArticleId()).isEqualTo(UPDATED_ARTICLE_ID);
        assertThat(testInvoiceLineItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testInvoiceLineItem.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testInvoiceLineItem.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testInvoiceLineItem.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testInvoiceLineItem.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testInvoiceLineItem.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testInvoiceLineItem.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceLineItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceLineItemRepository.findAll().size();
        invoiceLineItem.setInvoiceLineItemId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceLineItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceLineItem.getInvoiceLineItemId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceLineItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceLineItem in the database
        List<InvoiceLineItem> invoiceLineItemList = invoiceLineItemRepository.findAll();
        assertThat(invoiceLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceLineItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceLineItemRepository.findAll().size();
        invoiceLineItem.setInvoiceLineItemId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceLineItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceLineItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceLineItem in the database
        List<InvoiceLineItem> invoiceLineItemList = invoiceLineItemRepository.findAll();
        assertThat(invoiceLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceLineItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceLineItemRepository.findAll().size();
        invoiceLineItem.setInvoiceLineItemId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceLineItemMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceLineItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceLineItem in the database
        List<InvoiceLineItem> invoiceLineItemList = invoiceLineItemRepository.findAll();
        assertThat(invoiceLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceLineItemWithPatch() throws Exception {
        // Initialize the database
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem);

        int databaseSizeBeforeUpdate = invoiceLineItemRepository.findAll().size();

        // Update the invoiceLineItem using partial update
        InvoiceLineItem partialUpdatedInvoiceLineItem = new InvoiceLineItem();
        partialUpdatedInvoiceLineItem.setInvoiceLineItemId(invoiceLineItem.getInvoiceLineItemId());

        partialUpdatedInvoiceLineItem
            .articleType(UPDATED_ARTICLE_TYPE)
            .quantity(UPDATED_QUANTITY)
            .amount(UPDATED_AMOUNT)
            .discount(UPDATED_DISCOUNT)
            .updatedOn(UPDATED_UPDATED_ON);

        restInvoiceLineItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceLineItem.getInvoiceLineItemId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoiceLineItem))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceLineItem in the database
        List<InvoiceLineItem> invoiceLineItemList = invoiceLineItemRepository.findAll();
        assertThat(invoiceLineItemList).hasSize(databaseSizeBeforeUpdate);
        InvoiceLineItem testInvoiceLineItem = invoiceLineItemList.get(invoiceLineItemList.size() - 1);
        assertThat(testInvoiceLineItem.getArticleType()).isEqualTo(UPDATED_ARTICLE_TYPE);
        assertThat(testInvoiceLineItem.getArticleId()).isEqualTo(DEFAULT_ARTICLE_ID);
        assertThat(testInvoiceLineItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testInvoiceLineItem.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testInvoiceLineItem.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testInvoiceLineItem.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testInvoiceLineItem.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testInvoiceLineItem.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testInvoiceLineItem.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateInvoiceLineItemWithPatch() throws Exception {
        // Initialize the database
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem);

        int databaseSizeBeforeUpdate = invoiceLineItemRepository.findAll().size();

        // Update the invoiceLineItem using partial update
        InvoiceLineItem partialUpdatedInvoiceLineItem = new InvoiceLineItem();
        partialUpdatedInvoiceLineItem.setInvoiceLineItemId(invoiceLineItem.getInvoiceLineItemId());

        partialUpdatedInvoiceLineItem
            .articleType(UPDATED_ARTICLE_TYPE)
            .articleId(UPDATED_ARTICLE_ID)
            .quantity(UPDATED_QUANTITY)
            .amount(UPDATED_AMOUNT)
            .discount(UPDATED_DISCOUNT)
            .createdOn(UPDATED_CREATED_ON)
            .updatedOn(UPDATED_UPDATED_ON)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY);

        restInvoiceLineItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceLineItem.getInvoiceLineItemId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoiceLineItem))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceLineItem in the database
        List<InvoiceLineItem> invoiceLineItemList = invoiceLineItemRepository.findAll();
        assertThat(invoiceLineItemList).hasSize(databaseSizeBeforeUpdate);
        InvoiceLineItem testInvoiceLineItem = invoiceLineItemList.get(invoiceLineItemList.size() - 1);
        assertThat(testInvoiceLineItem.getArticleType()).isEqualTo(UPDATED_ARTICLE_TYPE);
        assertThat(testInvoiceLineItem.getArticleId()).isEqualTo(UPDATED_ARTICLE_ID);
        assertThat(testInvoiceLineItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testInvoiceLineItem.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testInvoiceLineItem.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testInvoiceLineItem.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testInvoiceLineItem.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testInvoiceLineItem.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testInvoiceLineItem.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceLineItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceLineItemRepository.findAll().size();
        invoiceLineItem.setInvoiceLineItemId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceLineItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceLineItem.getInvoiceLineItemId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceLineItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceLineItem in the database
        List<InvoiceLineItem> invoiceLineItemList = invoiceLineItemRepository.findAll();
        assertThat(invoiceLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceLineItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceLineItemRepository.findAll().size();
        invoiceLineItem.setInvoiceLineItemId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceLineItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceLineItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceLineItem in the database
        List<InvoiceLineItem> invoiceLineItemList = invoiceLineItemRepository.findAll();
        assertThat(invoiceLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceLineItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceLineItemRepository.findAll().size();
        invoiceLineItem.setInvoiceLineItemId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceLineItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceLineItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceLineItem in the database
        List<InvoiceLineItem> invoiceLineItemList = invoiceLineItemRepository.findAll();
        assertThat(invoiceLineItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceLineItem() throws Exception {
        // Initialize the database
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem);

        int databaseSizeBeforeDelete = invoiceLineItemRepository.findAll().size();

        // Delete the invoiceLineItem
        restInvoiceLineItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceLineItem.getInvoiceLineItemId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InvoiceLineItem> invoiceLineItemList = invoiceLineItemRepository.findAll();
        assertThat(invoiceLineItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
