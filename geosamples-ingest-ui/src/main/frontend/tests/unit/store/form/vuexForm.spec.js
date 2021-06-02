import * as vf from '@/store/form/vuexForm';
import {stateToObject} from "@/store/form/vuexForm";

const testModel = {
  id: 'int',
  version: 'int',
  organizationName: 'string',
  organizationDisplayName: 'string',
  website: 'string',
  uniqueIdPrefix: 'string',
  tokenSubject: 'string',
  enabled: 'boolean',
  contacts: [{
    id: 'int',
    version: 'int',
    firstName: 'string',
    lastName: 'string',
    otherOrganization: 'string',
    emailAddress: 'string',
    phone: 'string',
    street1: 'string',
    street2: 'string',
    city: 'string',
    state: 'string',
    zipCode: 'string',
    country: 'string',
  }],
  tokens: [{
    id: 'int',
    version: 'int',
    token: 'string',
    notAfter: 'date',
    notBefore: 'date',
    enabled: 'boolean',
  }],
  numbers: ['int'],
  nested: [{
    name: 'string',
    values: [
      {
        key: 'string',
        list: ['string'],
      },
    ],
  },
  ],
  matrix: [
    ['int'],
  ],
};

test('test new form', () => {
  const state = vf.defineState();
  vf.initializeState(state, testModel);
  const expected = {
    initialForm: {
      id: '',
      version: '',
      organizationName: '',
      organizationDisplayName: '',
      website: '',
      uniqueIdPrefix: '',
      tokenSubject: '',
      enabled: '',
      contacts: [],
      tokens: [],
      numbers: [],
      nested: [],
      matrix: [],
    },
    form: {
      id: {
        value: '', touched: false, error: '',
      },
      version: {
        value: '', touched: false, error: '',
      },
      organizationName: {
        value: '', touched: false, error: '',
      },
      organizationDisplayName: {
        value: '', touched: false, error: '',
      },
      website: {
        value: '', touched: false, error: '',
      },
      uniqueIdPrefix: {
        value: '', touched: false, error: '',
      },
      tokenSubject: {
        value: '', touched: false, error: '',
      },
      enabled: {
        value: '', touched: false, error: '',
      },
      contacts: [],
      tokens: [],
      numbers: [],
      nested: [],
      matrix: [],
    },
  };
  expect(state).toEqual(expected);
});

test('test initialize form', () => {
  const state = vf.defineState();

  const init = {
    id: 5,
    version: 6,
    organizationName: 'My Org',
    organizationDisplayName: 'My Org2',
    website: 'website1',
    uniqueIdPrefix: 'prefix',
    tokenSubject: 'sub',
    enabled: true,
    contacts: [{
      id: 7,
      version: 8,
      firstName: 'Bill',
      lastName: 'Bonzai',
      otherOrganization: 'bill org',
      emailAddress: 'billadd',
      phone: 'billphone',
      street1: 'billst1',
      street2: 'billst2',
      city: 'billcity',
      state: 'billstate',
      zipCode: 'billzip',
      country: 'billcountry',
    },
    {
      id: 9,
      version: 10,
      firstName: 'Fred',
      lastName: 'Ficus',
      emailAddress: null,
    }],
    tokens: [{
      id: 11,
      version: 12,
      token: 'tok1',
      notAfter: '2022-01-12T18:58:16Z',
      notBefore: '2021-01-12T18:58:16Z',
      enabled: false,
    }],
    numbers: [32, 42],
    nested: [{
      name: 'nest1',
      values: [
        {
          key: 'val1',
          list: ['cat', 'dog'],
        },
        {
          key: 'val2',
          list: ['fish', 'snake'],
        },
      ],
    },
    {
      name: 'nest2',
      values: [],
    },
    ],
    matrix: [
      [1, 2, 3, 4],
      [5, 6, 7, 8],
    ],
  };

  vf.initializeState(state, testModel, init);
  const expected = {
    initialForm: {
      id: '5',
      version: '6',
      organizationName: 'My Org',
      organizationDisplayName: 'My Org2',
      website: 'website1',
      uniqueIdPrefix: 'prefix',
      tokenSubject: 'sub',
      enabled: 'true',
      contacts: [{
        id: '7',
        version: '8',
        firstName: 'Bill',
        lastName: 'Bonzai',
        otherOrganization: 'bill org',
        emailAddress: 'billadd',
        phone: 'billphone',
        street1: 'billst1',
        street2: 'billst2',
        city: 'billcity',
        state: 'billstate',
        zipCode: 'billzip',
        country: 'billcountry',
      },
      {
        id: '9',
        version: '10',
        firstName: 'Fred',
        lastName: 'Ficus',
        otherOrganization: '',
        emailAddress: '',
        phone: '',
        street1: '',
        street2: '',
        city: '',
        state: '',
        zipCode: '',
        country: '',
      }],
      tokens: [{
        id: '11',
        version: '12',
        token: 'tok1',
        notAfter: '2022-01-12T18:58:16Z',
        notBefore: '2021-01-12T18:58:16Z',
        enabled: 'false',
      }],
      numbers: ['32', '42'],
      nested: [{
        name: 'nest1',
        values: [
          {
            key: 'val1',
            list: ['cat', 'dog'],
          },
          {
            key: 'val2',
            list: ['fish', 'snake'],
          },
        ],
      },
      {
        name: 'nest2',
        values: [],
      },
      ],
      matrix: [
        ['1', '2', '3', '4'],
        ['5', '6', '7', '8'],
      ],
    },
    form: {
      id: {
        value: '5', touched: false, error: '',
      },
      version: {
        value: '6', touched: false, error: '',
      },
      organizationName: {
        value: 'My Org', touched: false, error: '',
      },
      organizationDisplayName: {
        value: 'My Org2', touched: false, error: '',
      },
      website: {
        value: 'website1', touched: false, error: '',
      },
      uniqueIdPrefix: {
        value: 'prefix', touched: false, error: '',
      },
      tokenSubject: {
        value: 'sub', touched: false, error: '',
      },
      enabled: {
        value: 'true', touched: false, error: '',
      },
      contacts: [
        {
          id: {
            value: '7', touched: false, error: '',
          },
          version: {
            value: '8', touched: false, error: '',
          },
          firstName: {
            value: 'Bill', touched: false, error: '',
          },
          lastName: {
            value: 'Bonzai', touched: false, error: '',
          },
          otherOrganization: {
            value: 'bill org', touched: false, error: '',
          },
          emailAddress: {
            value: 'billadd', touched: false, error: '',
          },
          phone: {
            value: 'billphone', touched: false, error: '',
          },
          street1: {
            value: 'billst1', touched: false, error: '',
          },
          street2: {
            value: 'billst2', touched: false, error: '',
          },
          city: {
            value: 'billcity', touched: false, error: '',
          },
          state: {
            value: 'billstate', touched: false, error: '',
          },
          zipCode: {
            value: 'billzip', touched: false, error: '',
          },
          country: {
            value: 'billcountry', touched: false, error: '',
          },
        },
        {
          id: {
            value: '9', touched: false, error: '',
          },
          version: {
            value: '10', touched: false, error: '',
          },
          firstName: {
            value: 'Fred', touched: false, error: '',
          },
          lastName: {
            value: 'Ficus', touched: false, error: '',
          },
          otherOrganization: {
            value: '', touched: false, error: '',
          },
          emailAddress: {
            value: '', touched: false, error: '',
          },
          phone: {
            value: '', touched: false, error: '',
          },
          street1: {
            value: '', touched: false, error: '',
          },
          street2: {
            value: '', touched: false, error: '',
          },
          city: {
            value: '', touched: false, error: '',
          },
          state: {
            value: '', touched: false, error: '',
          },
          zipCode: {
            value: '', touched: false, error: '',
          },
          country: {
            value: '', touched: false, error: '',
          },
        },
      ],
      tokens: [
        {
          id: {
            value: '11', touched: false, error: '',
          },
          version: {
            value: '12', touched: false, error: '',
          },
          token: {
            value: 'tok1', touched: false, error: '',
          },
          notAfter: {
            value: '2022-01-12T18:58:16Z', touched: false, error: '',
          },
          notBefore: {
            value: '2021-01-12T18:58:16Z', touched: false, error: '',
          },
          enabled: {
            value: 'false', touched: false, error: '',
          },
        },
      ],
      numbers: [
        {
          value: '32', touched: false, error: '',
        },
        {
          value: '42', touched: false, error: '',
        },
      ],
      nested: [
        {
          name: {
            value: 'nest1', touched: false, error: '',
          },
          values: [
            {
              key: {
                value: 'val1', touched: false, error: '',
              },
              list: [
                {
                  value: 'cat', touched: false, error: '',
                },
                {
                  value: 'dog', touched: false, error: '',
                },
              ],
            },
            {
              key: {
                value: 'val2', touched: false, error: '',
              },
              list: [
                {
                  value: 'fish', touched: false, error: '',
                },
                {
                  value: 'snake', touched: false, error: '',
                },
              ],
            },
          ],
        },
        {
          name: {
            value: 'nest2', touched: false, error: '',
          },
          values: [],
        },
      ],
      matrix: [
        [{
          value: '1', touched: false, error: '',
        },
        {
          value: '2', touched: false, error: '',
        },
        {
          value: '3', touched: false, error: '',
        },
        {
          value: '4', touched: false, error: '',
        }],
        [
          {
            value: '5', touched: false, error: '',
          },
          {
            value: '6', touched: false, error: '',
          },
          {
            value: '7', touched: false, error: '',
          },
          {
            value: '8', touched: false, error: '',
          },
        ],
      ],
    },
  };
  console.log(state);
  expect(state).toEqual(expected);
});

test('test add array', () => {
  const state = vf.defineState();
  const init = {
    id: 5,
    version: 6,
    organizationName: 'My Org',
    organizationDisplayName: 'My Org2',
    website: 'website1',
    uniqueIdPrefix: 'prefix',
    tokenSubject: 'sub',
    enabled: true,
    contacts: [{
      id: 7,
      version: 8,
      firstName: 'Bill',
      lastName: 'Bonzai',
      otherOrganization: 'bill org',
      emailAddress: 'billadd',
      phone: 'billphone',
      street1: 'billst1',
      street2: 'billst2',
      city: 'billcity',
      state: 'billstate',
      zipCode: 'billzip',
      country: 'billcountry',
    },
    {
      id: 9,
      version: 10,
      firstName: 'Fred',
      lastName: 'Ficus',
      emailAddress: null,
    }],
    tokens: [{
      id: 11,
      version: 12,
      token: 'tok1',
      notAfter: '2022-01-12T18:58:16Z',
      notBefore: '2021-01-12T18:58:16Z',
      enabled: false,
    }],
    numbers: [32, 42],
    nested: [{
      name: 'nest1',
      values: [
        {
          key: 'val1',
          list: ['cat', 'dog'],
        },
        {
          key: 'val2',
          list: ['fish', 'snake'],
        },
      ],
    },
    {
      name: 'nest2',
      values: [],
    },
    ],
    matrix: [
      [1, 2, 3, 4],
      [5, 6, 7, 8],
    ],
  };
  vf.initializeState(state, testModel, init);

  vf.addToArray(state, testModel, 'numbers', '75');
  vf.addToArray(state, testModel, 'tokens', {
    id: '55',
    version: '56',
    token: 'tok3',
    notAfter: '2023-01-12T18:58:16Z',
    notBefore: '2024-01-12T18:58:16Z',
    enabled: 'true',
  });
  vf.addToArray(state, testModel, 'matrix', ['9', '10', '11', '12']);
  vf.addToArray(state, testModel, 'matrix[1]', '88');
  vf.addToArray(state, testModel, 'nested[0].values[1].list', 'foo');

  const expectedNumbers = [
    {
      value: '32', touched: false, error: '',
    },
    {
      value: '42', touched: false, error: '',
    },
    {
      value: '75', touched: true, error: '',
    },
  ];
  expect(state.form.numbers).toEqual(expectedNumbers);

  const expectedTokens = [
    {
      id: {
        value: '11', touched: false, error: '',
      },
      version: {
        value: '12', touched: false, error: '',
      },
      token: {
        value: 'tok1', touched: false, error: '',
      },
      notAfter: {
        value: '2022-01-12T18:58:16Z', touched: false, error: '',
      },
      notBefore: {
        value: '2021-01-12T18:58:16Z', touched: false, error: '',
      },
      enabled: {
        value: 'false', touched: false, error: '',
      },
    },
    {
      id: {
        value: '55', touched: true, error: '',
      },
      version: {
        value: '56', touched: true, error: '',
      },
      token: {
        value: 'tok3', touched: true, error: '',
      },
      notAfter: {
        value: '2023-01-12T18:58:16Z', touched: true, error: '',
      },
      notBefore: {
        value: '2024-01-12T18:58:16Z', touched: true, error: '',
      },
      enabled: {
        value: 'true', touched: true, error: '',
      },
    },
  ];
  expect(state.form.tokens).toEqual(expectedTokens);

  const expectedMatrix = [
    [
      {
        value: '1', touched: false, error: '',
      },
      {
        value: '2', touched: false, error: '',
      },
      {
        value: '3', touched: false, error: '',
      },
      {
        value: '4', touched: false, error: '',
      },
    ],
    [
      {
        value: '5', touched: false, error: '',
      },
      {
        value: '6', touched: false, error: '',
      },
      {
        value: '7', touched: false, error: '',
      },
      {
        value: '8', touched: false, error: '',
      },
      {
        value: '88', touched: true, error: '',
      },
    ],
    [
      {
        value: '9', touched: true, error: '',
      },
      {
        value: '10', touched: true, error: '',
      },
      {
        value: '11', touched: true, error: '',
      },
      {
        value: '12', touched: true, error: '',
      },
    ],
  ];
  expect(state.form.matrix).toEqual(expectedMatrix);

  const expectedNested = [
    {
      name: {
        value: 'nest1', touched: false, error: '',
      },
      values: [
        {
          key: {
            value: 'val1', touched: false, error: '',
          },
          list: [
            {
              value: 'cat', touched: false, error: '',
            },
            {
              value: 'dog', touched: false, error: '',
            },
          ],
        },
        {
          key: {
            value: 'val2', touched: false, error: '',
          },
          list: [
            {
              value: 'fish', touched: false, error: '',
            },
            {
              value: 'snake', touched: false, error: '',
            },
            {
              value: 'foo', touched: true, error: '',
            },
          ],
        },
      ],
    },
    {
      name: {
        value: 'nest2', touched: false, error: '',
      },
      values: [],
    },
  ];
  expect(state.form.nested).toEqual(expectedNested);
});

test('test delete array', () => {
  const state = vf.defineState();
  const init = {
    id: 5,
    version: 6,
    organizationName: 'My Org',
    organizationDisplayName: 'My Org2',
    website: 'website1',
    uniqueIdPrefix: 'prefix',
    tokenSubject: 'sub',
    enabled: true,
    contacts: [{
      id: 7,
      version: 8,
      firstName: 'Bill',
      lastName: 'Bonzai',
      otherOrganization: 'bill org',
      emailAddress: 'billadd',
      phone: 'billphone',
      street1: 'billst1',
      street2: 'billst2',
      city: 'billcity',
      state: 'billstate',
      zipCode: 'billzip',
      country: 'billcountry',
    },
    {
      id: 9,
      version: 10,
      firstName: 'Fred',
      lastName: 'Ficus',
      emailAddress: null,
    }],
    tokens: [{
      id: 11,
      version: 12,
      token: 'tok1',
      notAfter: '2022-01-12T18:58:16Z',
      notBefore: '2021-01-12T18:58:16Z',
      enabled: false,
    }],
    numbers: [32, 42],
    nested: [{
      name: 'nest1',
      values: [
        {
          key: 'val1',
          list: ['cat', 'dog'],
        },
        {
          key: 'val2',
          list: ['fish', 'snake'],
        },
      ],
    },
    {
      name: 'nest2',
      values: [],
    },
    ],
    matrix: [
      [1, 2, 3, 4],
      [5, 6, 7, 8],
    ],
  };
  vf.initializeState(state, testModel, init);

  vf.deleteFromArray(state, 'numbers[1]');
  vf.deleteFromArray(state, 'contacts[0]');
  vf.deleteFromArray(state, 'nested[0].values[1].list[0]');
  vf.deleteFromArray(state, 'matrix[1]');
  vf.deleteFromArray(state, 'matrix[0][1]');

  const expectedNumbers = [
    {
      value: '32', touched: false, error: '',
    },
  ];
  expect(state.form.numbers).toEqual(expectedNumbers);

  const expectedContacts = [
    {
      id: {
        value: '9', touched: false, error: '',
      },
      version: {
        value: '10', touched: false, error: '',
      },
      firstName: {
        value: 'Fred', touched: false, error: '',
      },
      lastName: {
        value: 'Ficus', touched: false, error: '',
      },
      otherOrganization: {
        value: '', touched: false, error: '',
      },
      emailAddress: {
        value: '', touched: false, error: '',
      },
      phone: {
        value: '', touched: false, error: '',
      },
      street1: {
        value: '', touched: false, error: '',
      },
      street2: {
        value: '', touched: false, error: '',
      },
      city: {
        value: '', touched: false, error: '',
      },
      state: {
        value: '', touched: false, error: '',
      },
      zipCode: {
        value: '', touched: false, error: '',
      },
      country: {
        value: '', touched: false, error: '',
      },
    },
  ];
  expect(state.form.contacts).toEqual(expectedContacts);

  const expectedMatrix = [
    [
      {
        value: '1', touched: false, error: '',
      },
      {
        value: '3', touched: false, error: '',
      },
      {
        value: '4', touched: false, error: '',
      },
    ],
  ];
  expect(state.form.matrix).toEqual(expectedMatrix);

  const expectedNested = [
    {
      name: {
        value: 'nest1', touched: false, error: '',
      },
      values: [
        {
          key: {
            value: 'val1', touched: false, error: '',
          },
          list: [
            {
              value: 'cat', touched: false, error: '',
            },
            {
              value: 'dog', touched: false, error: '',
            },
          ],
        },
        {
          key: {
            value: 'val2', touched: false, error: '',
          },
          list: [
            {
              value: 'snake', touched: false, error: '',
            },
          ],
        },
      ],
    },
    {
      name: {
        value: 'nest2', touched: false, error: '',
      },
      values: [],
    },
  ];
  expect(state.form.nested).toEqual(expectedNested);
});

test('test update value', () => {
  const state = vf.defineState();
  const init = {
    id: 5,
    version: 6,
    organizationName: 'My Org',
    organizationDisplayName: 'My Org2',
    website: 'website1',
    uniqueIdPrefix: 'prefix',
    tokenSubject: 'sub',
    enabled: true,
    contacts: [{
      id: 7,
      version: 8,
      firstName: 'Bill',
      lastName: 'Bonzai',
      otherOrganization: 'bill org',
      emailAddress: 'billadd',
      phone: 'billphone',
      street1: 'billst1',
      street2: 'billst2',
      city: 'billcity',
      state: 'billstate',
      zipCode: 'billzip',
      country: 'billcountry',
    },
    {
      id: 9,
      version: 10,
      firstName: 'Fred',
      lastName: 'Ficus',
      emailAddress: null,
    }],
    tokens: [{
      id: 11,
      version: 12,
      token: 'tok1',
      notAfter: '2022-01-12T18:58:16Z',
      notBefore: '2021-01-12T18:58:16Z',
      enabled: false,
    }],
    numbers: [32, 42],
    nested: [{
      name: 'nest1',
      values: [
        {
          key: 'val1',
          list: ['cat', 'dog'],
        },
        {
          key: 'val2',
          list: ['fish', 'snake'],
        },
      ],
    },
    {
      name: 'nest2',
      values: [],
    },
    ],
    matrix: [
      [1, 2, 3, 4],
      [5, 6, 7, 8],
    ],
  };
  vf.initializeState(state, testModel, init);

  vf.setValue(state, 'organizationName', 'neworg');
  vf.setValue(state, 'nested[0].values[1].list[0]', 'lizard');
  vf.setValue(state, 'nested[1].name', 'newnested');
  vf.setValue(state, 'matrix[1][1]', '66');
  vf.setValue(state, 'numbers[1]', '77');

  expect(vf.getValue(state, 'organizationName', testModel)).toBe('neworg');
  expect(vf.isDirty(state, 'organizationName')).toBe(true);
  expect(vf.isTouched(state, 'organizationName')).toBe(true);

  expect(vf.getValue(state, 'nested[0].values[1].list[0]', testModel)).toBe('lizard');
  expect(vf.isDirty(state, 'nested[0].values[1].list[0]')).toBe(true);
  expect(vf.isTouched(state, 'nested[0].values[1].list[0]')).toBe(true);

  expect(vf.getValue(state, 'nested[1].name', testModel)).toBe('newnested');
  expect(vf.isDirty(state, 'nested[1].name')).toBe(true);
  expect(vf.isTouched(state, 'nested[1].name')).toBe(true);

  expect(vf.getValue(state, 'matrix[1][1]', testModel)).toBe('66');
  expect(vf.isDirty(state, 'matrix[1][1]')).toBe(true);
  expect(vf.isTouched(state, 'matrix[1][1]')).toBe(true);

  expect(vf.getValue(state, 'numbers[1]', testModel)).toBe('77');
  expect(vf.isDirty(state, 'numbers[1]')).toBe(true);
  expect(vf.isTouched(state, 'numbers[1]')).toBe(true);
});

test('test touched', () => {
  const state = vf.defineState();
  const init = {
    id: 5,
    version: 6,
    organizationName: 'My Org',
    organizationDisplayName: 'My Org2',
    website: 'website1',
    uniqueIdPrefix: 'prefix',
    tokenSubject: 'sub',
    enabled: true,
    contacts: [{
      id: 7,
      version: 8,
      firstName: 'Bill',
      lastName: 'Bonzai',
      otherOrganization: 'bill org',
      emailAddress: 'billadd',
      phone: 'billphone',
      street1: 'billst1',
      street2: 'billst2',
      city: 'billcity',
      state: 'billstate',
      zipCode: 'billzip',
      country: 'billcountry',
    },
    {
      id: 9,
      version: 10,
      firstName: 'Fred',
      lastName: 'Ficus',
      emailAddress: null,
    }],
    tokens: [{
      id: 11,
      version: 12,
      token: 'tok1',
      notAfter: '2022-01-12T18:58:16Z',
      notBefore: '2021-01-12T18:58:16Z',
      enabled: false,
    }],
    numbers: [32, 42],
    nested: [{
      name: 'nest1',
      values: [
        {
          key: 'val1',
          list: ['cat', 'dog'],
        },
        {
          key: 'val2',
          list: ['fish', 'snake'],
        },
      ],
    },
    {
      name: 'nest2',
      values: [],
    },
    ],
    matrix: [
      [1, 2, 3, 4],
      [5, 6, 7, 8],
    ],
  };
  vf.initializeState(state, testModel, init);
  vf.setTouched(state, 'organizationName');
  vf.setTouched(state, 'nested[0].values[1].list[0]');
  vf.setTouched(state, 'nested[1].name');
  vf.setTouched(state, 'matrix[1][1]');
  vf.setTouched(state, 'numbers[1]');

  expect(vf.isTouched(state, 'organizationName')).toBe(true);
  expect(vf.isTouched(state, 'nested[0].values[1].list[0]')).toBe(true);
  expect(vf.isTouched(state, 'nested[1].name')).toBe(true);
  expect(vf.isTouched(state, 'matrix[1][1]')).toBe(true);
  expect(vf.isTouched(state, 'numbers[1]')).toBe(true);
});

test('test error', () => {
  const state = vf.defineState();
  const init = {
    id: 5,
    version: 6,
    organizationName: 'My Org',
    organizationDisplayName: 'My Org2',
    website: 'website1',
    uniqueIdPrefix: 'prefix',
    tokenSubject: 'sub',
    enabled: true,
    contacts: [{
      id: 7,
      version: 8,
      firstName: 'Bill',
      lastName: 'Bonzai',
      otherOrganization: 'bill org',
      emailAddress: 'billadd',
      phone: 'billphone',
      street1: 'billst1',
      street2: 'billst2',
      city: 'billcity',
      state: 'billstate',
      zipCode: 'billzip',
      country: 'billcountry',
    },
    {
      id: 9,
      version: 10,
      firstName: 'Fred',
      lastName: 'Ficus',
      emailAddress: null,
    }],
    tokens: [{
      id: 11,
      version: 12,
      token: 'tok1',
      notAfter: '2022-01-12T18:58:16Z',
      notBefore: '2021-01-12T18:58:16Z',
      enabled: false,
    }],
    numbers: [32, 42],
    nested: [{
      name: 'nest1',
      values: [
        {
          key: 'val1',
          list: ['cat', 'dog'],
        },
        {
          key: 'val2',
          list: ['fish', 'snake'],
        },
      ],
    },
    {
      name: 'nest2',
      values: [],
    },
    ],
    matrix: [
      [1, 2, 3, 4],
      [5, 6, 7, 8],
    ],
  };
  vf.initializeState(state, testModel, init);
  vf.setError(state, 'organizationName', 'organizationNameErr');
  vf.setError(state, 'nested[0].values[1].list[0]', 'nestedErr1');
  vf.setError(state, 'nested[1].name', 'nestedErr2');
  vf.setError(state, 'matrix[1][1]', 'matrixErr');
  vf.setError(state, 'numbers[1]', 'numbersErr');

  expect(vf.getError(state, 'organizationName')).toBe('organizationNameErr');
  expect(vf.getError(state, 'nested[0].values[1].list[0]')).toBe('nestedErr1');
  expect(vf.getError(state, 'nested[1].name')).toBe('nestedErr2');
  expect(vf.getError(state, 'matrix[1][1]')).toBe('matrixErr');
  expect(vf.getError(state, 'numbers[1]')).toBe('numbersErr');

  vf.clearAllErrors(state);
  expect(vf.getError(state, 'organizationName')).toBe('');
  expect(vf.getError(state, 'nested[0].values[1].list[0]')).toBe('');
  expect(vf.getError(state, 'nested[1].name')).toBe('');
  expect(vf.getError(state, 'matrix[1][1]')).toBe('');
  expect(vf.getError(state, 'numbers[1]')).toBe('');
});

test('test touch all', () => {
  const state = vf.defineState();
  const init = {
    id: 5,
    version: 6,
    organizationName: 'My Org',
    organizationDisplayName: 'My Org2',
    website: 'website1',
    uniqueIdPrefix: 'prefix',
    tokenSubject: 'sub',
    enabled: true,
    contacts: [{
      id: 7,
      version: 8,
      firstName: 'Bill',
      lastName: 'Bonzai',
      otherOrganization: 'bill org',
      emailAddress: 'billadd',
      phone: 'billphone',
      street1: 'billst1',
      street2: 'billst2',
      city: 'billcity',
      state: 'billstate',
      zipCode: 'billzip',
      country: 'billcountry',
    },
    {
      id: 9,
      version: 10,
      firstName: 'Fred',
      lastName: 'Ficus',
      emailAddress: null,
    }],
    tokens: [{
      id: 11,
      version: 12,
      token: 'tok1',
      notAfter: '2022-01-12T18:58:16Z',
      notBefore: '2021-01-12T18:58:16Z',
      enabled: false,
    }],
    numbers: [32, 42],
    nested: [{
      name: 'nest1',
      values: [
        {
          key: 'val1',
          list: ['cat', 'dog'],
        },
        {
          key: 'val2',
          list: ['fish', 'snake'],
        },
      ],
    },
    {
      name: 'nest2',
      values: [],
    },
    ],
    matrix: [
      [1, 2, 3, 4],
      [5, 6, 7, 8],
    ],
  };
  vf.initializeState(state, testModel, init);
  vf.touchAll(state);

  expect(vf.isTouched(state, 'organizationName')).toBe(true);
  expect(vf.isTouched(state, 'nested[0].values[1].list[0]')).toBe(true);
  expect(vf.isTouched(state, 'nested[1].name')).toBe(true);
  expect(vf.isTouched(state, 'matrix[1][1]')).toBe(true);
  expect(vf.isTouched(state, 'numbers[1]')).toBe(true);
});

test('test submit', () => {
  const state = vf.defineState();
  const init = {
    id: 5,
    version: 6,
    organizationName: 'My Org',
    organizationDisplayName: 'My Org2',
    website: 'website1',
    uniqueIdPrefix: 'prefix',
    tokenSubject: 'sub',
    enabled: true,
    contacts: [{
      id: 7,
      version: 8,
      firstName: 'Bill',
      lastName: 'Bonzai',
      otherOrganization: 'bill org',
      emailAddress: 'billadd',
      phone: 'billphone',
      street1: 'billst1',
      street2: 'billst2',
      city: 'billcity',
      state: 'billstate',
      zipCode: 'billzip',
      country: 'billcountry',
    },
    {
      id: 9,
      version: 10,
      firstName: 'Fred',
      lastName: 'Ficus',
      emailAddress: null,
    }],
    tokens: [{
      id: 11,
      version: 12,
      token: 'tok1',
      notAfter: '2022-01-12T18:58:16Z',
      notBefore: '2021-01-12T18:58:16Z',
      enabled: false,
    }],
    numbers: [32, 42],
    nested: [{
      name: 'nest1',
      values: [
        {
          key: 'val1',
          list: ['cat', 'dog'],
        },
        {
          key: 'val2',
          list: ['fish', 'snake'],
        },
      ],
    },
    {
      name: 'nest2',
      values: [],
    },
    ],
    matrix: [
      [1, 2, 3, 4],
      [5, 6, 7, 8],
    ],
  };
  vf.initializeState(state, testModel, init);

  vf.setValue(state, 'organizationName', 'neworg');
  vf.setValue(state, 'nested[0].values[1].list[0]', 'lizard');
  vf.setValue(state, 'nested[1].name', 'newnested');
  vf.setValue(state, 'matrix[1][1]', '66');
  vf.setValue(state, 'numbers[1]', '77');

  vf.addToArray(state, testModel, 'matrix', ['9', '10', '11', '12']);
  vf.addToArray(state, testModel, 'matrix[1]', '88');
  vf.addToArray(state, testModel, 'nested[0].values[1].list', 'foo');

  vf.deleteFromArray(state, 'numbers[0]');

  const result = vf.stateToObject(state, testModel);

  const expected = {
    id: 5,
    version: 6,
    organizationName: 'neworg',
    organizationDisplayName: 'My Org2',
    website: 'website1',
    uniqueIdPrefix: 'prefix',
    tokenSubject: 'sub',
    enabled: true,
    contacts: [{
      id: 7,
      version: 8,
      firstName: 'Bill',
      lastName: 'Bonzai',
      otherOrganization: 'bill org',
      emailAddress: 'billadd',
      phone: 'billphone',
      street1: 'billst1',
      street2: 'billst2',
      city: 'billcity',
      state: 'billstate',
      zipCode: 'billzip',
      country: 'billcountry',
    },
    {
      id: 9,
      version: 10,
      firstName: 'Fred',
      lastName: 'Ficus',
    }],
    tokens: [{
      id: 11,
      version: 12,
      token: 'tok1',
      notAfter: '2022-01-12T18:58:16.000Z',
      notBefore: '2021-01-12T18:58:16.000Z',
      enabled: false,
    }],
    numbers: [77],
    nested: [{
      name: 'nest1',
      values: [
        {
          key: 'val1',
          list: ['cat', 'dog'],
        },
        {
          key: 'val2',
          list: ['lizard', 'snake', 'foo'],
        },
      ],
    },
    {
      name: 'newnested',
    },
    ],
    matrix: [
      [1, 2, 3, 4],
      [5, 66, 7, 8, 88],
      [9, 10, 11, 12],
    ],
  };

  expect(result).toEqual(expected);

  expect(vf.isTouched(state, 'organizationName')).toBe(false);
  expect(vf.isTouched(state, 'nested[0].values[1].list[0]')).toBe(false);
  expect(vf.isTouched(state, 'nested[1].name')).toBe(false);
  expect(vf.isTouched(state, 'matrix[1][1]')).toBe(false);
  expect(vf.isTouched(state, 'numbers[0]')).toBe(false);
});

test('test reset', () => {
  const state = vf.defineState();
  const init = {
    id: 5,
    version: 6,
    organizationName: 'My Org',
    organizationDisplayName: 'My Org2',
    website: 'website1',
    uniqueIdPrefix: 'prefix',
    tokenSubject: 'sub',
    enabled: true,
    contacts: [{
      id: 7,
      version: 8,
      firstName: 'Bill',
      lastName: 'Bonzai',
      otherOrganization: 'bill org',
      emailAddress: 'billadd',
      phone: 'billphone',
      street1: 'billst1',
      street2: 'billst2',
      city: 'billcity',
      state: 'billstate',
      zipCode: 'billzip',
      country: 'billcountry',
    },
    {
      id: 9,
      version: 10,
      firstName: 'Fred',
      lastName: 'Ficus',
      emailAddress: null,
    }],
    tokens: [{
      id: 11,
      version: 12,
      token: 'tok1',
      notAfter: '2022-01-12T18:58:16Z',
      notBefore: '2021-01-12T18:58:16Z',
      enabled: false,
    }],
    numbers: [32, 42],
    nested: [{
      name: 'nest1',
      values: [
        {
          key: 'val1',
          list: ['cat', 'dog'],
        },
        {
          key: 'val2',
          list: ['fish', 'snake'],
        },
      ],
    },
    {
      name: 'nest2',
      values: [],
    },
    ],
    matrix: [
      [1, 2, 3, 4],
      [5, 6, 7, 8],
    ],
  };
  vf.initializeState(state, testModel, init);

  vf.setValue(state, 'organizationName', 'neworg');
  vf.setValue(state, 'nested[0].values[1].list[0]', 'lizard');
  vf.setValue(state, 'nested[1].name', 'newnested');
  vf.setValue(state, 'matrix[1][1]', '66');
  vf.setValue(state, 'numbers[1]', '77');

  vf.addToArray(state, testModel, 'matrix', ['9', '10', '11', '12']);
  vf.addToArray(state, testModel, 'matrix[1]', '88');
  vf.addToArray(state, testModel, 'nested[0].values[1].list', 'foo');

  vf.deleteFromArray(state, 'numbers[0]');

  vf.reset(state, testModel);

  const result = vf.submit(state, testModel);

  const expected = {
    id: 5,
    version: 6,
    organizationName: 'My Org',
    organizationDisplayName: 'My Org2',
    website: 'website1',
    uniqueIdPrefix: 'prefix',
    tokenSubject: 'sub',
    enabled: true,
    contacts: [{
      id: 7,
      version: 8,
      firstName: 'Bill',
      lastName: 'Bonzai',
      otherOrganization: 'bill org',
      emailAddress: 'billadd',
      phone: 'billphone',
      street1: 'billst1',
      street2: 'billst2',
      city: 'billcity',
      state: 'billstate',
      zipCode: 'billzip',
      country: 'billcountry',
    },
    {
      id: 9,
      version: 10,
      firstName: 'Fred',
      lastName: 'Ficus',
    }],
    tokens: [{
      id: 11,
      version: 12,
      token: 'tok1',
      notAfter: '2022-01-12T18:58:16.000Z',
      notBefore: '2021-01-12T18:58:16.000Z',
      enabled: false,
    }],
    numbers: [32, 42],
    nested: [{
      name: 'nest1',
      values: [
        {
          key: 'val1',
          list: ['cat', 'dog'],
        },
        {
          key: 'val2',
          list: ['fish', 'snake'],
        },
      ],
    },
    {
      name: 'nest2',
    },
    ],
    matrix: [
      [1, 2, 3, 4],
      [5, 6, 7, 8],
    ],
  };

  expect(result).toEqual(expected);
});

test('test form dirty first level', () => {
  const state = vf.defineState();
  const init = {
    id: 5,
    version: 6,
    organizationName: 'My Org',
    organizationDisplayName: 'My Org2',
    website: 'website1',
    uniqueIdPrefix: 'prefix',
    tokenSubject: 'sub',
    enabled: true,
    contacts: [{
      id: 7,
      version: 8,
      firstName: 'Bill',
      lastName: 'Bonzai',
      otherOrganization: 'bill org',
      emailAddress: 'billadd',
      phone: 'billphone',
      street1: 'billst1',
      street2: 'billst2',
      city: 'billcity',
      state: 'billstate',
      zipCode: 'billzip',
      country: 'billcountry',
    },
    {
      id: 9,
      version: 10,
      firstName: 'Fred',
      lastName: 'Ficus',
      emailAddress: null,
    }],
    tokens: [{
      id: 11,
      version: 12,
      token: 'tok1',
      notAfter: '2022-01-12T18:58:16Z',
      notBefore: '2021-01-12T18:58:16Z',
      enabled: false,
    }],
    numbers: [32, 42],
    nested: [{
      name: 'nest1',
      values: [
        {
          key: 'val1',
          list: ['cat', 'dog'],
        },
        {
          key: 'val2',
          list: ['fish', 'snake'],
        },
      ],
    },
    {
      name: 'nest2',
      values: [],
    },
    ],
    matrix: [
      [1, 2, 3, 4],
      [5, 6, 7, 8],
    ],
  };
  vf.initializeState(state, testModel, init);

  expect(vf.isFormDirty(state, testModel)).toBe(false);

  vf.setValue(state, 'organizationName', 'neworg');

  expect(vf.isFormDirty(state, testModel)).toBe(true);
});

test('test form dirty array value', () => {
  const state = vf.defineState();
  const init = {
    id: 5,
    version: 6,
    organizationName: 'My Org',
    organizationDisplayName: 'My Org2',
    website: 'website1',
    uniqueIdPrefix: 'prefix',
    tokenSubject: 'sub',
    enabled: true,
    contacts: [{
      id: 7,
      version: 8,
      firstName: 'Bill',
      lastName: 'Bonzai',
      otherOrganization: 'bill org',
      emailAddress: 'billadd',
      phone: 'billphone',
      street1: 'billst1',
      street2: 'billst2',
      city: 'billcity',
      state: 'billstate',
      zipCode: 'billzip',
      country: 'billcountry',
    },
    {
      id: 9,
      version: 10,
      firstName: 'Fred',
      lastName: 'Ficus',
      emailAddress: null,
    }],
    tokens: [{
      id: 11,
      version: 12,
      token: 'tok1',
      notAfter: '2022-01-12T18:58:16Z',
      notBefore: '2021-01-12T18:58:16Z',
      enabled: false,
    }],
    numbers: [32, 42],
    nested: [{
      name: 'nest1',
      values: [
        {
          key: 'val1',
          list: ['cat', 'dog'],
        },
        {
          key: 'val2',
          list: ['fish', 'snake'],
        },
      ],
    },
    {
      name: 'nest2',
      values: [],
    },
    ],
    matrix: [
      [1, 2, 3, 4],
      [5, 6, 7, 8],
    ],
  };
  vf.initializeState(state, testModel, init);

  expect(vf.isFormDirty(state, testModel)).toBe(false);

  vf.setValue(state, 'matrix[1][1]', '66');

  expect(vf.isFormDirty(state, testModel)).toBe(true);
});

test('test form dirty array size', () => {
  const state = vf.defineState();
  const init = {
    id: 5,
    version: 6,
    organizationName: 'My Org',
    organizationDisplayName: 'My Org2',
    website: 'website1',
    uniqueIdPrefix: 'prefix',
    tokenSubject: 'sub',
    enabled: true,
    contacts: [{
      id: 7,
      version: 8,
      firstName: 'Bill',
      lastName: 'Bonzai',
      otherOrganization: 'bill org',
      emailAddress: 'billadd',
      phone: 'billphone',
      street1: 'billst1',
      street2: 'billst2',
      city: 'billcity',
      state: 'billstate',
      zipCode: 'billzip',
      country: 'billcountry',
    },
    {
      id: 9,
      version: 10,
      firstName: 'Fred',
      lastName: 'Ficus',
      emailAddress: null,
    }],
    tokens: [{
      id: 11,
      version: 12,
      token: 'tok1',
      notAfter: '2022-01-12T18:58:16Z',
      notBefore: '2021-01-12T18:58:16Z',
      enabled: false,
    }],
    numbers: [32, 42],
    nested: [{
      name: 'nest1',
      values: [
        {
          key: 'val1',
          list: ['cat', 'dog'],
        },
        {
          key: 'val2',
          list: ['fish', 'snake'],
        },
      ],
    },
    {
      name: 'nest2',
      values: [],
    },
    ],
    matrix: [
      [1, 2, 3, 4],
      [5, 6, 7, 8],
    ],
  };
  vf.initializeState(state, testModel, init);

  expect(vf.isFormDirty(state, testModel)).toBe(false);

  vf.addToArray(state, testModel, 'nested[0].values[1].list', 'foo');

  expect(vf.isFormDirty(state, testModel)).toBe(true);
});

test('test form dirty matrix size ', () => {
  const state = vf.defineState();
  const init = {
    id: 5,
    version: 6,
    organizationName: 'My Org',
    organizationDisplayName: 'My Org2',
    website: 'website1',
    uniqueIdPrefix: 'prefix',
    tokenSubject: 'sub',
    enabled: true,
    contacts: [{
      id: 7,
      version: 8,
      firstName: 'Bill',
      lastName: 'Bonzai',
      otherOrganization: 'bill org',
      emailAddress: 'billadd',
      phone: 'billphone',
      street1: 'billst1',
      street2: 'billst2',
      city: 'billcity',
      state: 'billstate',
      zipCode: 'billzip',
      country: 'billcountry',
    },
    {
      id: 9,
      version: 10,
      firstName: 'Fred',
      lastName: 'Ficus',
      emailAddress: null,
    }],
    tokens: [{
      id: 11,
      version: 12,
      token: 'tok1',
      notAfter: '2022-01-12T18:58:16Z',
      notBefore: '2021-01-12T18:58:16Z',
      enabled: false,
    }],
    numbers: [32, 42],
    nested: [{
      name: 'nest1',
      values: [
        {
          key: 'val1',
          list: ['cat', 'dog'],
        },
        {
          key: 'val2',
          list: ['fish', 'snake'],
        },
      ],
    },
    {
      name: 'nest2',
      values: [],
    },
    ],
    matrix: [
      [1, 2, 3, 4],
      [5, 6, 7, 8],
    ],
  };
  vf.initializeState(state, testModel, init);

  expect(vf.isFormDirty(state, testModel)).toBe(false);

  vf.addToArray(state, testModel, 'matrix', ['9', '10', '11', '12']);

  expect(vf.isFormDirty(state, testModel)).toBe(true);
});

test('test form dirty nested', () => {
  const state = vf.defineState();
  const init = {
    id: 5,
    version: 6,
    organizationName: 'My Org',
    organizationDisplayName: 'My Org2',
    website: 'website1',
    uniqueIdPrefix: 'prefix',
    tokenSubject: 'sub',
    enabled: true,
    contacts: [{
      id: 7,
      version: 8,
      firstName: 'Bill',
      lastName: 'Bonzai',
      otherOrganization: 'bill org',
      emailAddress: 'billadd',
      phone: 'billphone',
      street1: 'billst1',
      street2: 'billst2',
      city: 'billcity',
      state: 'billstate',
      zipCode: 'billzip',
      country: 'billcountry',
    },
    {
      id: 9,
      version: 10,
      firstName: 'Fred',
      lastName: 'Ficus',
      emailAddress: null,
    }],
    tokens: [{
      id: 11,
      version: 12,
      token: 'tok1',
      notAfter: '2022-01-12T18:58:16Z',
      notBefore: '2021-01-12T18:58:16Z',
      enabled: false,
    }],
    numbers: [32, 42],
    nested: [{
      name: 'nest1',
      values: [
        {
          key: 'val1',
          list: ['cat', 'dog'],
        },
        {
          key: 'val2',
          list: ['fish', 'snake'],
        },
      ],
    },
    {
      name: 'nest2',
      values: [],
    },
    ],
    matrix: [
      [1, 2, 3, 4],
      [5, 6, 7, 8],
    ],
  };
  vf.initializeState(state, testModel, init);

  expect(vf.isFormDirty(state, testModel)).toBe(false);

  vf.setValue(state, 'nested[0].values[1].key', 'newkey');

  expect(vf.isFormDirty(state, testModel)).toBe(true);
});
