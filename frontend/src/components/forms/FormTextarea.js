import React from 'react';

const FormTextarea = ({
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
			<textarea
				className={`form-control ${isFaulty ? 'is-invalid' : ''}`}
				rows="3"
				placeholder={placeholder}
				{...input}
			/>
			{isFaulty && <div className="invalid-feedback">{error}</div>}
		</div>
	);
};

export default FormTextarea;
