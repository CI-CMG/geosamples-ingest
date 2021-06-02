import dot from 'dot-object';

const getType = (key, model) => {
  const obj = model[key];
  if (Array.isArray(obj)) {
    return 'array';
  }
  if (typeof obj === 'object') {
    return 'object';
  }
  return obj;
};

const initialFormValue = (value) => {
  const v = value == null ? '' : value;
  return `${v}`;
};

const formValue = (value) => {
  const v = value == null ? '' : value;
  return {
    value: `${v}`,
    touched: false,
    error: '',
  };
};

const buildObject = (form, initialForm, model, initial) => {
  if (Array.isArray(form)) {
    const type = getType(0, model);
    if (initial) {
      initial.forEach((initialItem) => {
        let formObj;
        let initialFormObj;
        switch (type) {
          case 'array':
            formObj = [];
            initialFormObj = [];
            break;
          case 'object':
            formObj = {};
            initialFormObj = {};
            break;
          default:
            formObj = formValue(initialItem);
            initialFormObj = initialFormValue(initialItem);
            break;
        }
        form.push(formObj);
        if (initialForm) {
          initialForm.push(initialFormObj);
        }
        if (type === 'array' || type === 'object') {
          buildObject(formObj, initialFormObj, model[0], initialItem);
        }
      });
    }
  } else {
    const keys = Object.keys(model);
    keys.forEach((key) => {
      const type = getType(key, model);
      const value = initial ? initial[key] : '';
      let formObj;
      let initialFormObj;
      switch (type) {
        case 'array':
          formObj = [];
          initialFormObj = [];
          break;
        case 'object':
          formObj = {};
          initialFormObj = {};
          break;
        default:
          formObj = formValue(value);
          initialFormObj = initialFormValue(value);
          break;
      }
      form[key] = formObj;
      if (initialForm) {
        initialForm[key] = initialFormObj;
      }
      if (initial && (type === 'array' || type === 'object')) {
        buildObject(formObj, initialFormObj, model[key], initial[key]);
      }
    });
  }
};

export const initializeState = (state, model, initial) => {
  const form = {};
  const initialForm = {};
  buildObject(form, initialForm, model, initial);
  state.form = form;
  state.initialForm = initialForm;
};

export const defineState = () => ({
  form: {},
  initialForm: {},
});

export const touchAll = (obj, touched = true) => {
  if (Array.isArray(obj)) {
    obj.forEach((item) => touchAll(item, touched));
  } else {
    const paths = Object.keys(dot.dot(obj));
    paths.filter((path) => path.endsWith('.touched'))
      .forEach((path) => {
        dot.set(path, touched, obj);
      });
  }
};

export const clearAllErrors = (obj) => {
  if (Array.isArray(obj)) {
    obj.forEach((item) => clearAllErrors(item));
  } else {
    const paths = Object.keys(dot.dot(obj));
    paths.filter((path) => path.endsWith('.error'))
      .forEach((path) => {
        dot.set(path, '', obj);
      });
  }
};

export const addToArray = (state, model, path, item) => {
  // console.log(state.form);
  // console.log(model);
  // console.log(path);
  // console.log(item);
  const array = dot.pick(path, state.form);
  const subModel = dot.pick(path.replace(/\[\d+]/g, '[0]'), model);
  const expanded = [];
  buildObject(expanded, null, subModel, [item]);
  touchAll({ a: expanded[0] });
  array.push(expanded[0]);
};

export const deleteFromArray = (state, path) => {
  const parts = path.match(/(.+)\[(\d+)]$/);
  const array = dot.pick(parts[1], state.form);
  array.splice(parseInt(parts[2], 10), 1);
};

export const setValue = (state, path, value) => {
  const newValue = `${value}`.trim();
  dot.set(`${path}.touched`, true, state.form);
  dot.set(`${path}.value`, newValue, state.form);
};

export const getValue = (state, path, model) => {
  const type = getType(path, model);
  let resolvedPath;
  switch (type) {
    case 'array':
    case 'object':
      resolvedPath = path;
      break;
    default:
      resolvedPath = `${path}.value`;
  }
  return dot.pick(resolvedPath, state.form);
};

export const isTouched = (state, path) => dot.pick(`${path}.touched`, state.form);

export const setTouched = (state, path, touched = true) => dot.set(`${path}.touched`, touched,
  state.form);

export const isDirty = (state, path) => {
  const fromVal = dot.pick(`${path}.value`, state.form);
  const initVal = dot.pick(path, state.initialForm);
  return fromVal !== initVal;
};
export const getError = (state, path) => dot.pick(`${path}.error`, state.form);

export const setError = (state, path, message) => dot.set(`${path}.error`, message || '',
  state.form);

const convert = (model, path, str) => {
  const type = dot.pick(path.replace(/\[\d+\]/g, '[0]'), model);
  switch (type) {
    case 'string': {
      if (str == null || !str.trim()) {
        return null;
      }
      return `${str.trim()}`;
    }
    case 'int': {
      if (str == null || !str.trim()) {
        return null;
      }
      return parseInt(str.trim(), 10);
    }
    case 'float': {
      if (str == null || !str.trim()) {
        return null;
      }
      return parseFloat(str.trim());
    }
    case 'boolean': {
      if (str == null) {
        return false;
      }
      const trimmed = str.trim().toLowerCase();
      if (!trimmed) {
        return false;
      }
      return ['true', 'y', 'yes', '1'].indexOf(trimmed) >= 0;
    }
    case 'date': {
      if (str == null || !str.trim()) {
        return null;
      }
      const d = new Date(str.trim());
      return d.toISOString();
    }
    default:
      return null;
  }
};

export const stateToObject = (state, model) => {
  const paths = Object.keys(dot.dot(state.form));
  const obj = {};
  paths
    .filter((path) => path.endsWith('.value'))
    .forEach((path) => {
      const outPath = path.replace(/\.value$/g, '');
      const str = dot.pick(path, state.form);
      const converted = convert(model, outPath, str);
      if (converted != null) {
        dot.set(outPath, converted, obj);
      }
    });
  return obj;
};

export const reset = (state, model) => {
  const form = {};
  buildObject(form, null, model, state.initialForm);
  state.form = form;
};

const checkForm = (form, initialForm, model, validator, checkLength) => {
  if (Array.isArray(model)) {
    if (checkLength && form.length !== initialForm.length) {
      return true;
    }
    const type = getType(0, model);
    switch (type) {
      case 'array':
      case 'object': {
        for (let i = 0; i < form.length; i += 1) {
          if (!initialForm[i]) {
            return checkLength;
          }
          if (checkForm(form[i], initialForm[i], model[0], validator, checkLength)) {
            return true;
          }
        }
        break;
      }
      default: {
        for (let i = 0; i < form.length; i += 1) {
          if (validator(form[i], initialForm[i])) {
            return true;
          }
        }
        break;
      }
    }
  } else {
    if (!form || Object.keys(form).length === 0) {
      return false;
    }
    const keys = Object.keys(model);
    for (let i = 0; i < keys.length; i += 1) {
      const key = keys[i];
      const type = getType(key, model);
      const formObj = form[key];
      const initialFormObj = initialForm[key];
      switch (type) {
        case 'array':
        case 'object':
          if (checkForm(formObj, initialFormObj, model[key], validator, checkLength)) {
            return true;
          }
          break;
        default:
          if (validator(formObj, initialFormObj)) {
            return true;
          }
          break;
      }
    }
  }
  return false;
};

const checkValue = (formObj, initialValue) => formObj.value !== initialValue;
const checkError = (formObj) => formObj.error && !formObj.touched;

export const isFormDirty = (state, model) => checkForm(state.form, state.initialForm, model, checkValue, true);

export const isFormUntouchedErrors = (state, model) => checkForm(state.form, state.initialForm, model, checkError, false);

export const mapValue = (getter, mutator, path, mutatorPayloadGenerator = (p, v) => ({ path: p, value: v })) => ({
  get() {
    return this.$store.getters[getter](path);
  },
  set(value) {
    this.$store.commit(mutator, mutatorPayloadGenerator(path, value));
  },
});
