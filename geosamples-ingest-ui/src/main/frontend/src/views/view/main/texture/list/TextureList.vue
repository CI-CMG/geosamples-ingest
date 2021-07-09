<template>
<div>
  <b-form @submit.prevent="search" @reset.prevent="reset">
    <b-container fluid>
      <b-row>
        <b-col>
          <b-form-group label="Texture" :label-for="textureId">
            <b-form-input :id="textureId" v-model="texture"/>
          </b-form-group>
        </b-col>
        <b-col>
          <b-form-group label="Texture Code" :label-for="textureCodeId">
            <b-form-input :id="textureCodeId" v-model="textureCode"/>
          </b-form-group>
        </b-col>
      </b-row>
    </b-container>

    <div v-if="!searching">
      <b-button type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Search</b-button>
      <b-button type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Clear</b-button>
    </div>
  </b-form>
  <b-button :to="{ name: 'TextureAdd' }" variant="secondary" class="m-3">Add New Texture</b-button>
  <b-table
    striped
    bordered
    small
    hover
    :items="items"
    :fields="fields"
    no-local-sorting
    @sort-changed="sortChanged"
    :sort-by="sortBy"
    :sort-desc="sortDesc">
    <template #cell(texture)="data">
      <b-link :to="{ name: 'TextureEdit', params: { id: data.item.texture }}">{{ data.item.texture }}</b-link>
    </template>
  </b-table>
  <b-pagination v-model="currentPage" @input="changePage" :total-rows="totalItems" per-page="50"></b-pagination>
</div>
</template>

<script>

import genId from '@/components/idGenerator';
import {
  mapActions, mapMutations, mapState,
} from 'vuex';

export default {
  beforeMount() {
    this.textureId = genId();
    this.textureCodeId = genId();
  },
  beforeRouteEnter(to, from, next) {
    next((self) => {
      self.search();
    });
  },
  beforeRouteUpdate(to, from, next) {
    this.search();
    next();
  },
  beforeRouteLeave(to, from, next) {
    this.clearAll();
    next();
  },

  methods: {
    ...mapMutations('texture', ['setTexture', 'clearParams', 'setTextureCode', 'firstPage', 'setPage', 'setSortBy', 'setSortDesc', 'clearAll']),
    ...mapActions('texture', ['search', 'reset', 'changePage']),
    sortChanged({ sortBy, sortDesc }) {
      this.setSortBy(sortBy);
      this.setSortDesc(sortDesc);
      this.search();
    },
  },

  computed: {
    ...mapState('texture', ['searching', 'page', 'totalItems', 'totalPages', 'items', 'params', 'sortDesc', 'sortBy']),
    texture: {
      get() {
        return this.params.texture;
      },
      set(value) {
        this.setTexture(value);
      },
    },
    textureCode: {
      get() {
        return this.params.textureCode;
      },
      set(value) {
        this.setTextureCode(value);
      },
    },
    currentPage: {
      get() {
        return this.page;
      },
      set(value) {
        this.setPage(value);
      },
    },
  },

  data() {
    return {
      textureId: null,
      textureCodeId: null,

      fields: [
        {
          key: 'texture',
          label: 'Texture',
          sortable: true,
        },
        {
          key: 'textureCode',
          label: 'Texture Code',
          sortable: true,
        },
      ],
    };
  },
};
</script>
