import DictTag from './DictTag/index.vue'
import DictSelect from './DictSelect/index.vue'
import DictRadio from './DictRadio/index.vue'

export function setupComponents(app) {
    app.component('DictTag', DictTag)
    app.component('DictSelect', DictSelect)
    app.component('DictRadio', DictRadio)
} 