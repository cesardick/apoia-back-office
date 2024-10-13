package br.com.apoia.web.rest;

import static br.com.apoia.domain.AreaAsserts.*;
import static br.com.apoia.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.apoia.IntegrationTest;
import br.com.apoia.domain.Area;
import br.com.apoia.repository.AreaRepository;
import br.com.apoia.service.AreaService;
import br.com.apoia.service.dto.AreaDTO;
import br.com.apoia.service.mapper.AreaMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AreaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AreaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/areas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AreaRepository areaRepository;

    @Mock
    private AreaRepository areaRepositoryMock;

    @Autowired
    private AreaMapper areaMapper;

    @Mock
    private AreaService areaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAreaMockMvc;

    private Area area;

    private Area insertedArea;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Area createEntity() {
        return new Area().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Area createUpdatedEntity() {
        return new Area().name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        area = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedArea != null) {
            areaRepository.delete(insertedArea);
            insertedArea = null;
        }
    }

    @Test
    @Transactional
    void createArea() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Area
        AreaDTO areaDTO = areaMapper.toDto(area);
        var returnedAreaDTO = om.readValue(
            restAreaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(areaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AreaDTO.class
        );

        // Validate the Area in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedArea = areaMapper.toEntity(returnedAreaDTO);
        assertAreaUpdatableFieldsEquals(returnedArea, getPersistedArea(returnedArea));

        insertedArea = returnedArea;
    }

    @Test
    @Transactional
    void createAreaWithExistingId() throws Exception {
        // Create the Area with an existing ID
        insertedArea = areaRepository.saveAndFlush(area);
        AreaDTO areaDTO = areaMapper.toDto(area);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAreaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(areaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Area in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAreas() throws Exception {
        // Initialize the database
        insertedArea = areaRepository.saveAndFlush(area);

        // Get all the areaList
        restAreaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(area.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAreasWithEagerRelationshipsIsEnabled() throws Exception {
        when(areaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAreaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(areaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAreasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(areaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAreaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(areaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getArea() throws Exception {
        // Initialize the database
        insertedArea = areaRepository.saveAndFlush(area);

        // Get the area
        restAreaMockMvc
            .perform(get(ENTITY_API_URL_ID, area.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(area.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingArea() throws Exception {
        // Get the area
        restAreaMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArea() throws Exception {
        // Initialize the database
        insertedArea = areaRepository.saveAndFlush(area);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the area
        Area updatedArea = areaRepository.findById(area.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedArea are not directly saved in db
        em.detach(updatedArea);
        updatedArea.name(UPDATED_NAME);
        AreaDTO areaDTO = areaMapper.toDto(updatedArea);

        restAreaMockMvc
            .perform(put(ENTITY_API_URL_ID, areaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(areaDTO)))
            .andExpect(status().isOk());

        // Validate the Area in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAreaToMatchAllProperties(updatedArea);
    }

    @Test
    @Transactional
    void putNonExistingArea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        area.setId(UUID.randomUUID());

        // Create the Area
        AreaDTO areaDTO = areaMapper.toDto(area);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAreaMockMvc
            .perform(put(ENTITY_API_URL_ID, areaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(areaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Area in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        area.setId(UUID.randomUUID());

        // Create the Area
        AreaDTO areaDTO = areaMapper.toDto(area);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAreaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(areaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Area in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        area.setId(UUID.randomUUID());

        // Create the Area
        AreaDTO areaDTO = areaMapper.toDto(area);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAreaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(areaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Area in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAreaWithPatch() throws Exception {
        // Initialize the database
        insertedArea = areaRepository.saveAndFlush(area);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the area using partial update
        Area partialUpdatedArea = new Area();
        partialUpdatedArea.setId(area.getId());

        restAreaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArea.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArea))
            )
            .andExpect(status().isOk());

        // Validate the Area in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAreaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedArea, area), getPersistedArea(area));
    }

    @Test
    @Transactional
    void fullUpdateAreaWithPatch() throws Exception {
        // Initialize the database
        insertedArea = areaRepository.saveAndFlush(area);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the area using partial update
        Area partialUpdatedArea = new Area();
        partialUpdatedArea.setId(area.getId());

        partialUpdatedArea.name(UPDATED_NAME);

        restAreaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArea.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArea))
            )
            .andExpect(status().isOk());

        // Validate the Area in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAreaUpdatableFieldsEquals(partialUpdatedArea, getPersistedArea(partialUpdatedArea));
    }

    @Test
    @Transactional
    void patchNonExistingArea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        area.setId(UUID.randomUUID());

        // Create the Area
        AreaDTO areaDTO = areaMapper.toDto(area);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAreaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, areaDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(areaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Area in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        area.setId(UUID.randomUUID());

        // Create the Area
        AreaDTO areaDTO = areaMapper.toDto(area);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAreaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(areaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Area in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArea() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        area.setId(UUID.randomUUID());

        // Create the Area
        AreaDTO areaDTO = areaMapper.toDto(area);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAreaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(areaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Area in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArea() throws Exception {
        // Initialize the database
        insertedArea = areaRepository.saveAndFlush(area);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the area
        restAreaMockMvc
            .perform(delete(ENTITY_API_URL_ID, area.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return areaRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Area getPersistedArea(Area area) {
        return areaRepository.findById(area.getId()).orElseThrow();
    }

    protected void assertPersistedAreaToMatchAllProperties(Area expectedArea) {
        assertAreaAllPropertiesEquals(expectedArea, getPersistedArea(expectedArea));
    }

    protected void assertPersistedAreaToMatchUpdatableProperties(Area expectedArea) {
        assertAreaAllUpdatablePropertiesEquals(expectedArea, getPersistedArea(expectedArea));
    }
}
