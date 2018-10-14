import { formValidation } from '../forms';

it('should return error if value is not set', () => {
  const errors = formValidation(
    [],
    [{ name: 'first_name', error: 'You must write a first name' }]
  );

  expect(errors).toHaveProperty('first_name');
  expect(errors.first_name).toContain('You must write a first name');
});

it('should not return error if value is set', () => {
  const errors = formValidation(
    [{ first_name: 'John' }],
    [{ name: 'first_name', error: 'You must write a first name' }]
  );

  // Empty object
  expect(errors).toMatchObject({});
});
