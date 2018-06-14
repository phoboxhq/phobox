import Vue from 'vue'
import Router from 'vue-router'
import FileBrowser from '@/components/files/FileBrowser'
import Approval from '@/components/approval/Approval'
import Search from '@/components/Search'
import AlbumBrowser from '@/components/album/AlbumBrowser'
import Status from '@/components/Status'
import Upload from '@/components/Upload'
import Settings from '@/components/settings/Settings'
import About from '@/components/About'

Vue.use(Router)

export default new Router({
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
