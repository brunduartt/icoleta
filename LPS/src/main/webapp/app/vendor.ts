/* after changing this file run 'npm run webpack:build' */
import '../content/scss/vendor.scss';
import 'leaflet/dist/leaflet.js';

import * as L from 'leaflet';

L.Icon.Default.imagePath = '.';
const prototype = L.Icon.Default.prototype as any;
delete prototype._getIconUrl;

L.Icon.Default.mergeOptions({
  iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
  iconUrl: require('leaflet/dist/images/marker-icon.png'),
  shadowUrl: require('leaflet/dist/images/marker-shadow.png')
});
