export const defaultMoviesState = {
	list: [],
	count: 0,
	filter: {
		query: null,
		offset: 0,
		limit: 20,
		sortProperty: 'release',
		direction: -1,
	},
	loading: true,
	error: null,
};
