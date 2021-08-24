export function encodeSlash(v) {
  return v.replaceAll('/', '*|*');
}
