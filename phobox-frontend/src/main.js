// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import VueI18n from 'vue-i18n'
import Language from './language'

Vue.config.productionTip = false

Vue.use(VueI18n)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  i18n: new VueI18n({
    locale: Language.getBrowserLocale(),
    messages: Language.messages
  }),
  components: { App },
  template: '<App/>'
})

String.prototype.replaceAll = function(search, replacement) {
  var target = this;
  return target.split(search).join(replacement);
};
