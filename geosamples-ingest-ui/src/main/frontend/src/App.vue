<template>
  <div>
    <div class="crowbar-alert">
      <b-alert v-for="error in errors" :key="error.id"
        :show="error.countDown"
        dismissible
        variant="danger"
        @dismissed="() => dismissAlert(error.id)"
        @dismiss-count-down="(countDown) => countDownChanged({ countDown, id: error.id })"
      >
        <p>{{error.msg}}</p>
        <b-progress variant="danger" :value="error.countDown" max="20" height="4px"></b-progress>
      </b-alert>
    </div>

    <router-view/>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';

export default {
  methods: {
    ...mapActions({
      countDownChanged: 'app/countDownChanged',
      dismissAlert: 'app/dismissAlert',
    }),
  },
  computed: {
    ...mapGetters({
      errors: 'app/errors',
    }),
  },
};
</script>
