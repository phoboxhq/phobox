const router = new VueRouter({
	routes: [
		{
			path: '/photos/',
			redirect: '/photos/%252F',
			component: FileBrowser,
			alias: '/'
		},
		{
			path: '/photos/:path?',
			name: 'path',
			component: FileBrowser,
		},
		{
			path: '/approval/',
			component: Approval,
		},
		{
			path: '/search/',
			component: Search,
		},
		{
			path: '/albums/',
			component: AlbumBrowser,
		},
		{
			path: '/albums/:albumpath?',
			name: 'albumpath',
			component: AlbumBrowser,
		},
		{
			path: '/status',
			component: Status,
		},
		{
			path: '/upload',
			component: Upload,
		},
		{
			path: '/settings',
			component: Settings,
		},
		{
			path: '/about',
			component: About,
		},
	],
})
