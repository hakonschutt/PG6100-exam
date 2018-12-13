import React from 'react';

const FormField = ({
	input,
	type,
	label,
	placeholder,
	meta: { error, touched },
}) => {
	const isFaulty = touched && error;

	return (
		<div className="form-group">
			<label>{label}</label>
			<input
				type="text"
				className={`form-control ${isFaulty ? 'is-invalid' : ''}`}
				placeholder={placeholder}
				{...input}
			/>
			{isFaulty && <div className="invalid-feedback">{error}</div>}
		</div>
	);
};

export default FormField;
