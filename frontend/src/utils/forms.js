export function formValidation(values, formFields) {
	const errors = {};

	formFields.forEach(field => {
		if (!values[field.name]) {
			errors[field.name] = field.error;
		}
	});

	return errors;
}
