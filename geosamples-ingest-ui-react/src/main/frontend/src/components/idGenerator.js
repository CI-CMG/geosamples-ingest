let id = 1;

export default () => {
  const value = `i${id}`;
  id += 1;
  return value;
};
