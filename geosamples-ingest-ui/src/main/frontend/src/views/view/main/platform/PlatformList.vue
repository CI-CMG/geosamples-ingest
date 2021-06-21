<template>
<div>
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
    :sort-desc="sortDesc"
  />
</div>
</template>

<script>
/*
  private Integer masterId;
  @Size(max = 30)
  private String prefix;
  @Size(max = 4)
  private String icesCode;
  @Size(max = 255)
  private String sourceUri;
 */

export default {
  data() {
    return {
      items: [],
      fields: [
        {
          key: 'masterId',
          label: 'Master ID',
          sortable: true,
        },
        {
          key: 'prefix',
          label: 'Prefix',
        },
        {
          key: 'platform',
          label: 'Platform',
          sortable: true,
        },
        {
          key: 'icesCode',
          label: 'ICES Code',
        },
        {
          key: 'sourceUri',
          label: 'Source URI',
        },
      ],
    };
  },
  methods: {
    sortChanged(ctx) {
      console.log(ctx);
    },
  },
};
</script>
