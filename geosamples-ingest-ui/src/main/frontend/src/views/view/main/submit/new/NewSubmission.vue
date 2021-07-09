<template>
  <b-card title="Import Submission For Preview">
    <div v-if="uploading">
      <b-spinner/> Processing
    </div>
    <b-form v-else @submit.prevent="() => uploadFile({ router: $router})" @reset.prevent="() => setFile(null)"><br />
      <b-form-file
        v-model="fileModel"
        :state="Boolean(file)"
        placeholder="Choose an Excel file or drop it here..."
        drop-placeholder="Drop Excel file here..."
      ></b-form-file>
      <div class="mt-2">
        <b-button v-if="Boolean(file)" type="submit" variant="primary" class="mb-2 mr-sm-2 mb-sm-0 mr-3">Upload</b-button>
        <b-button v-if="Boolean(file)" type="reset" variant="danger" class="mb-2 mr-sm-2 mb-sm-0">Cancel</b-button>
      </div>
    </b-form>
  </b-card>

</template>

<script>
import { mapActions, mapMutations, mapState } from 'vuex';

export default {
  computed: {
    ...mapState('submission', ['file', 'uploading']),
    fileModel: {
      get() {
        return this.file;
      },
      set(value) {
        this.setFile(value);
      },
    },
  },
  methods: {
    ...mapActions('submission', ['uploadFile']),
    ...mapMutations('submission', ['setFile']),
  },
};
</script>
