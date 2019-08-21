<template>
	<el-dialog title="批量导入黑名单" ref="modal" :visible.sync="modalVisible" :close-on-click-modal="false" 
        class="deivce-batch-add-dialog">

		<el-row :gutter="0">         
            <el-col :span="24">                
                <div class="cmcc-info-text">注意:导入的文件为excel文件</div>
                <div class="cmcc-info-text" style="margin-bottom:30px;">里面包含下面三列，从第二行开始导入数据<br>
                    姓名,身份证，性别<br>
                </div>

                <el-upload  style="float:right;"
                    ref="fileUpload"
                    name="uploadFile"
                    accept=".xls,.xlsx"
                    :multiple="false"
                    :action="batchAddUploadURL"
                    :on-success="uploadFileSuccess"
                    :on-error="uploadFileError"
                    :before-upload="uploadFileBeforeUpload">
                    <el-button type="primary" slot="trigger" size="small" :loading="loading">导入数据</el-button>
                </el-upload>
                       
            </el-col>
		</el-row>
	</el-dialog>
</template>

<script>
	import { mapGetters } from 'vuex';

	import { AdminAPI } from '../../api';

    import * as _ from 'lodash';

    export default {
        data() {
            return {
                modalVisible: true,
                loading: false,               
                batchAddUploadURL: AdminAPI.uploadBlackListUrl,               
            }
        },
        computed: {           
        },
        watch:{			
		},
        methods: {
            uploadFileSuccess(data) {
                if(data.status === 0) {
                    this.deviceInfoList = data.data.devices;
                    this.$message({
                        message: '文件导入成功!',
                        type: 'success',
                    });
                    this.$refs.fileUpload.clearFiles();
                } else {
                    this.$message.error(data.msg);
                }
            },
            uploadFileError(data) {
                this.$message.error('文件导入失败!');
            },
            uploadFileBeforeUpload(file) {
                const suffix = file.name.substring(file.name.length-3);
                if(suffix !== 'xls' && suffix !== 'xlsx'){
                    this.$message.error('选择excel文件上传');
                    return false;
                }
                return this.$confirm('是否确定导入文件?', '导入文件', {
                    confirmButtonText: '导入',
                    cancelButtonText: '取消',
                    type: 'info'
                });
            },
            save() {
               
            },
            cancel() {
               
            },
        },
    }
</script>
<style scoped lang="scss">
.el-col {
    height: 400px;
    position: relative;
}
.el-progress {
    position: absolute;
    bottom: 3em;
    left: 0;
    width: 100%;
}
.cmcc-action-wrap {
    margin: 5px 0 0;
    position: absolute;
    left: 0;
    bottom: 0;
    width: 100%;
    text-align: center;
}
.cmcc-info-text,
.cmcc-info-warn {
    font-size: 0.9em;
    margin-bottom: 5px;
}
.cmcc-info-warn {
    color: #f00;
}
.upload {
    display: inline-block;
}
</style>

<style lang="scss">
.deivce-batch-add-dialog {
    .el-dialog {
        width: 400px;
        height: 250px;
    }
}
</style>
