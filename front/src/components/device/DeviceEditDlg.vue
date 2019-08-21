<template>
	<!-- batch config -->
	<el-dialog :title="title" ref="modal"  width="30%" :visible.sync="modalVisible" :close-on-click-modal="false"
         class="edit-device-form">
         
		<el-form :model="batchEditForm" label-width="7em" :rules="batchEditFormRules" ref="batchEditForm">			
			<el-form-item label="设备名称" prop="name">
				<el-input type="text" v-model="batchEditForm.name" size="small" :value="deviceInfo.name"></el-input>
			</el-form-item>

            <el-form-item label="设备组" prop="group">
                <el-select v-model="group" placeholder="选择一个组">
                    <el-option v-for="item in groups"
                        :key="item.groupId"
                        :label="item.groupName"
                        :value="item.groupId">
                    </el-option>
                </el-select>
			</el-form-item>

		</el-form>
		<div slot="footer" class="dialog-footer">
			<el-button size="small" @click="modalVisible = false">取消</el-button>
			<el-button size="small" type="primary" @click="batchEdidtSubmit" :loading="batchConfigLoading">提交</el-button>
		</div>
	</el-dialog>
</template>

<script>
import { mapGetters } from 'vuex';
import { AdminAPI } from '../../api';

export default {
    data: function() {
        var nameCheck = (rule, value, callback) => { 
                        if(value === this.deviceInfo.name){                            
                            callback(new Error("输入不同的设备名称"));
                        }else{
                            callback();
                        }
                    };
        return {          
           modalVisible: true,			
           batchConfigLoading: false,           
           title: '',
           batchEditForm:{
               name:''
           },
           batchEditFormRules: {
                name: [
                    { required: true, message: '输入设备名称', trigger: 'change' },
                    //{validator : nameCheck, trigger: 'blur'}
                ]
           },
        };
    },
	computed: {
		
	},
    created() {                
        
        this.batchEditForm.name = this.deviceInfo.name;
        this.title = `设备编号: ${this.deviceInfo.deviceNo}`; 
    },
    methods: {
		batchEdidtSubmit() {            
			this.$refs.batchEditForm.validate(valid => {
				if(valid) {		                   	
					AdminAPI.updateDevice(
                        {name:this.batchEditForm.name, deviceNo:this.deviceInfo.deviceNo, groupId:this.group}
                    ).then(({data}) => {
						if(data.status === 0) {
							this.$message({
								message: '修改设备名称成功!'
							});
							this.$emit('data', {});
							this.modalVisible = false;
						} else {
							this.$message.error(data.message);
						}
						
					}).catch(() => {
						this.$message.error('修改设备信息失败!');
					});
				}
			});
		},
    },
}
</script>

<style scoped lang="scss">
.edit-device-form {
    .el-dialog {
        width: 300px;
    }
    .el-dialog__body {
        padding-top: 5px;
    }
    .el-form--inline .el-form-item__content {
        width: 150px;
    }
    .el-tabs__header {
        margin-bottom: 0;
    }
}
</style>
