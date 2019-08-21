<template>
	<div class="main-content" style="padding:3px;">
		<el-row :gutter=20 class="toolbar searchparam">
			
			<el-col  :span=5 class="search-action-wrap" style="margin-bottom:10px;">
				<div style="float:left">		
					<el-button size="small" @click="batchRemove" :disabled="this.sels.length === 0">删除</el-button>
					<el-button size="small" @click="refreshDevice">刷新</el-button>					
				</div>				
				
			</el-col>

			<el-col :span=15 class="paramleft">
				<el-form :inline="true" size="small" :model="searchForm" class="search-form" label-width="6em" ref="searchForm">
					<el-form-item label="" prop="name">
						<el-input size="small" v-model="searchForm.name" placeholder="设备名称"></el-input>
					</el-form-item>
					<el-form-item label="" prop="deviceNo">
						<el-input size="small" v-model="searchForm.deviceNo" placeholder="设备编号"></el-input>
					</el-form-item>
					<el-form-item label="" prop="status">
						<el-select clearable size="small" v-model="searchForm.status" placeholder="状态">
							<el-option  label="在线" value="0"></el-option>
							<el-option  label="离线" value="1"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="" prop="versionNo">						
						<el-input size="small" v-model="searchForm.versionNo" placeholder="版本"></el-input>						
					</el-form-item>					

					<el-button size="small" @click="searchDevice">查询</el-button>	
					<el-button size="small" @click="reset">清除</el-button>					
					
				</el-form>
			</el-col>
		</el-row>

		<section class="grid-content">
			<el-table :data="devices" resizable border highlight-current-row stripe v-loading="listLoading" ref="table" 
			 @selection-change="handleSelectionChange" 
			 @sort-change="handleSortChange"
			 @row-contextmenu="handleContextMenu" @row-dblclick="handleDblClickRow" class="cmcc-cell-nowrap">

				<el-table-column header-align="center"  type="selection">				
				</el-table-column>
				
				<el-table-column   prop="name" label="设备名称" width="240"></el-table-column>
				
				<el-table-column  prop="groupId" label="所属组" width="240">
					<template slot-scope="scope">
						{{scope.row.groupId | groupIdFormat(groupMaps)}}
					</template>
				</el-table-column>
				
				<el-table-column  prop="deviceNo" label="设备编号" width="200"></el-table-column>				
						
				<el-table-column  prop="status" label="设备状态" width="100">
					<template slot-scope="scope">						
						<el-tag :type="scope.row.status == '0' ? 'success' : 'danger'" close-transition>{{scope.row.status == '0'?'在线':'离线'}}</el-tag>					
					</template>
				</el-table-column>

				<el-table-column  sortable="custom" prop="todaySuccCount" label="今日认证" width="150">
				</el-table-column>				
				<el-table-column  sortable="custom" prop="totalSuccCount" label="认证总数" width="150">
				</el-table-column>	

				<el-table-column  prop="versionNo" label="客户端版本" >
				</el-table-column>			
				
			</el-table>
		</section>

		<el-col class="toolbar">			
			<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" 
				:current-page="page" :page-sizes="[10, 20, 30, 40, 50, 100]" 
				:page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
			</el-pagination>
		</el-col>
		
	</div>
</template>

<script>
	import axios from 'axios';
	import Vue from 'vue'
	import { mapGetters, mapActions } from 'vuex';
	import util from '../../common/js/util';
	import { AdminAPI } from '../../api';
	import { openModal } from '../../common/js/modal';

	import DeviceEditDlg from './DeviceEditDlg';
	//import DeviceGroupAddDlg from './BindDeviceDlg';
	const openDeviceEditDlg = openModal(DeviceEditDlg);
	//const openDeviceGroupAddDlg = openModal(DeviceGroupAddDlg);

	export default {
		data() {
			return {				
				searchForm: { 
					name:'',
					versionNo:'',
					deviceNo:'',
					status:''
				},
				groups:[],
				groupMaps:{},
				devices: [],
				total: 0,
				page: 1,				
				pageSize: 10,
				tableSort: {
					prop: 'sn',
					order: 'descending'
				},
				orderBy: [{
					name: 'sn',
					order: 'DESC'
				}],
				listLoading: false,
				sels: [], //列表选中列
				contextMenuRow: null,
				contextMenuPosition: {
					top: 0,
					left: 0
				},			

				// table event data
				deviceParamsConfigVisible: false,
				selectedDevice: null,
				sort:'name',
				order:'desc'
			}
		},
		filters: {
			groupIdFormat:(v,maps) => {return maps[v];}
		},
		computed: {

		},
		watch: {
			
		},
		methods: {		

			handleSortChange(col){		
				if(col.prop == null)
				{
					return;
				}		
				this.order = (col.order === 'ascending')? 'asc':'desc';
				this.sort = col.prop ;				
				this.searchDevice();
			},
			
			reset() {
				this.$refs.searchForm.resetFields();
			},
			
			searchDevice() {
				this.page = 1;
				this.getDeviceList();
			},
			
			refreshDevice() {
				this.getDeviceList();
			},
			
			handleSizeChange(size) {
				this.pageSize = size;				
				this.handleCurrentChange(1);
			},
			handleCurrentChange(val) {
				this.page = val;				
				this.getDeviceList();
			},
			getDeviceList() {
				var searchParams = _.omitBy(this.searchForm, (item) => item == "" || _.isNil(item));
				searchParams.page = this.page - 1;
				searchParams.size = this.pageSize;
				searchParams.sort=this.sort;//"deviceNo";
				searchParams.order=this.order;//"asc";
				
				this.listLoading = true;
				AdminAPI.searchDevice(searchParams).then(({
					data: jsonData
				}) => {
					if(jsonData.status === 0) {
						this.total = jsonData.data.total;
						this.devices = jsonData.data.content;
						this.total = jsonData.data.totalElements;
						this.listLoading = false;
					} else {
						this.$message({
							messsage: `获取设备列表失败:${data.msg}`,
							type: 'error'
						})
					}
				});
			},

			getDeviceGroup(){
				AdminAPI.getDeviceGroups().then(({
					data: jsonData
				}) => {
					this.getDeviceList();
					if(jsonData.status === 0) {
						this.groups = jsonData.data;
						if(this.groups.length <= 0)return;						
						 for(var i in this.groups){
							 this.groupMaps[this.groups[i].groupId] = this.groups[i].groupName;
						 }
					} else {
						this.$message({
							messsage: `获取组失败:${data.msg}`,
							type: 'error'
						})
					}
				});
			},
		
			// context menu
			handleSelectionChange: function(sels) {
				this.sels = sels;
				this.ids=_.map(this.sels , (device) => device.deviceNo);				
				//console.log(this.ids);
			},
			handleContextMenu(row, event) {
				
			},
			handleDblClickRow(row, event) {				
				openDeviceEditDlg({
					data: {
						deviceInfo: row,
						groups:this.groups,
						group:this.groupMaps[row.groupId]
					}
				}).then((data) => {
					// TODO: 拦截close事件,添加参数
					if(data !== undefined){
						this.getDeviceList();
					}
				});
			},
			

			handleContextMenuCommand(command) {				
			},
			getSelection() {
				let list;
				if(this.sels && this.sels.length > 0) {
					list = this.sels;
				} else {
					list = this.contextMenuRow ? [this.contextMenuRow] : [];
				}				
				return list;
			},			
		
			//批量删除
			batchRemove: function() {
				let ids =this.sels.map(item => item.deviceNo);
										
				this.$confirm('确认删除选中记录吗？', '提示', {
					type: 'warning'
				}).then(() => {					
					
					this.listLoading = true;
					AdminAPI.deleteDevices(
						ids
					).then(({data}) => {								
						if(data.status === 0){
							this.$message({
								message: '删除成功!',
								type: 'success'
							});
							this.getDeviceList();
						}else{
							this.$message({
								message: '删除失败!',
								type: 'error'
							});
						}
					}).catch((error) => {
						this.$message.error(error);
					});

					this.listLoading = false;
				}).catch((error) => {
					if(error instanceof Error) {
						this.$message.error('删除失败!'+error);
					}
				});
			},		
		},
		created() {
			
		},
		mounted() {
			this.getDeviceGroup();
			
			
		}
	}
</script>

<style scoped lang="scss">

</style>

<style lang="scss">
	.el-table th,
	.el-table__fixed-header-wrapper thead div,
	,
	.el-table__header-wrapper thead div {
		background-color: #177fd8;
		color: #fff;
	}
	
	.el-table .sort-caret.ascending {
		border-bottom: 5px solid #fff;
	}
	
	.el-table .sort-caret.descending {
		border-top: 5px solid #fff;
		border-top-color: #fff !important;
	}
	
	.searchparam {
		.el-button {
			border-radius: 6px;
		}
		@media only screen and (min-width: 1470px) {
			.paramleft {
				width: 75%;
			}
			
		}
		@media only screen and (max-width: 1470px) {
			.paramleft {
				width: 83%;
			}
			.search-action-wrap {
				width: 100%;
			}
		}
	}
	
	.add-device-form {
		.el-input,
		.el-select {
			width: 200px;
		}
		.el-dialog {
			width: 480px;
		}
		.el-form-item__tips {
			font-size: 12px;
			line-height: 1;
			padding-top: 4px;
			position: absolute;
			left: auto;
			right: 0;
			top: 10px;
		}
	}
	
	.device-params-picker {
		.el-input {
			width: 350px;
		}
		.el-dialog {
			width: 650px;
		}
		.el-form-item__error {
			top: 8px;
			left: 360px;
		}
	}
	
	.el-table {
		.el-button {
			background-color: #fff;
			color: #333;
		}
	}
</style>