import { Style, Circle as CircleStyle, Fill, Stroke } from 'ol/style';

// Status -> kolor markera
//   zielony  — wypożycza i ma rowery
//   pomarańcz — wypożycza, ale 0 rowerów (pusta)
//   szary    — nie wypożycza (offline / w serwisie)
export const COLORS = {
  available: '#0F8F4F',
  empty: '#E8643C',
  offline: '#8A8783',
  ink: '#15140F',
  paper: '#FAF7F0',
};

export function stationStatus(station) {
  if (station.isRenting === false) return 'offline';
  if (station.numBikesAvailable <= 0) return 'empty';
  return 'available';
}

export function stationLabel(status) {
  switch (status) {
    case 'available':
      return 'Dostępne';
    case 'empty':
      return 'Pusta';
    case 'offline':
      return 'Wyłączona';
    default:
      return 'Nieznane';
  }
}


const styleCache = new Map();

export function styleForStation(station, { selected = false, hovered = false } = {}) {
  const status = stationStatus(station);
  const color = COLORS[status];
  const key = `${status}|${selected ? 'S' : ''}|${hovered ? 'H' : ''}`;

  if (styleCache.has(key)) return styleCache.get(key);

  const radius = selected ? 11 : hovered ? 9 : 7;
  const strokeColor = selected ? COLORS.ink : '#FAF7F0';
  const strokeWidth = selected ? 3 : 2;

  const style = new Style({
    image: new CircleStyle({
      radius,
      fill: new Fill({ color }),
      stroke: new Stroke({ color: strokeColor, width: strokeWidth }),
    }),
  });

  styleCache.set(key, style);
  return style;
}
