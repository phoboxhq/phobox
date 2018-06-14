<template>
  <div id="settings_panel">
    <h1>{{ Locale.values.menu.settings }}</h1>
    <securityaccess></securityaccess>
    <importformat></importformat>
    <createthumbnails></createthumbnails>
    <changestorage></changestorage>
  </div>
</template>

<script>
export default {
  name: "Settings",
  data: function() {
    return {
      Locale: Locale
    };
  },
  computed: {},
  methods: {}
};
</script>

<style>
#settings_panel {
  max-width: 700px;
  margin: 0 auto;
  margin-top: -20px;

  padding: 0px 10px;
}

#settings_panel .panel {
  background-color: #191919;
  border-color: #191919;
}

#settings_panel .panel-default > .panel-heading {
  color: #fff;
  background-color: #131111;
  border-color: #000;
  font-weight: bold;
}

#settings_panel .alert-success {
  color: #f2fdf3;
  background-color: #7ca06d;
  border-color: #54654d;
}

#settings_panel .alert-danger {
  color: #f1fdf2;
  background-color: #9c5959;
  border-color: #5d2c2c;
}
</style>
