import tensorflow as tf
import numpy as np
import labelData as labelData

testdata = labelData.split_data(labelData.sitdatalist)
#normalize row data
data_x = np.array(labelData.distancelist)
mean = np.mean(data_x, axis=0)
data_x -= mean
std = np.std(data_x, axis=0)
data_x /= std
data_y = np.array(labelData.labellist)
LEARNING_RATE= 0.00001
# add layer function
def add_layer(inputs,in_size,out_size,activation_function = None):
    with tf.name_scope('layer'):
        with tf.name_scope('weights'):
            weights = tf.Variable(tf.random_normal([in_size,out_size]), name='weights')
        with tf.name_scope('biases'):
            biases = tf.Variable(tf.zeros([1,out_size])+0.1, name='biases')
        with tf.name_scope('Wx_plus_b'):
            Wx_plus_b = tf.matmul(inputs,weights)+biases
    if activation_function is None:
        outputs = Wx_plus_b
    else:
        outputs = activation_function(Wx_plus_b)
    return outputs

def compute_accuracy(v_xs,v_ys):
    global prediction
    y_pre = sess.run(prediction,feed_dict={xs:v_xs})
    correct_prediction = tf.equal(tf.argmax(y_pre,1),tf.argmax(v_ys,1))
    accuracy = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))
    result = sess.run(accuracy,feed_dict={xs:v_xs,ys:v_ys})
    return result
def prediction_classification(v_pc):
    global prediction
    y_pre = sess.run(prediction, feed_dict={xs: v_pc})
    accuracy = tf.argmax(tf.cast(y_pre, tf.float32), dimension=1)
    result = sess.run(accuracy,feed_dict={xs:v_pc})
    return result

#define placeholder for inputs to network
with tf.name_scope('inputs'):
    xs = tf.placeholder(tf.float32,[None,3]) #728
    ys = tf.placeholder(tf.float32,[None,4])
#a = np.random.randint(low=5, high=15, size=5)

#add hidden layer
with tf.name_scope('hidden'):
    h_l1 = add_layer(xs,3,5,activation_function = tf.nn.relu)
# h_l2 = add_layer(h_l1,5,10,activation_function = tf.nn.relu)
# h_l3 = add_layer(h_l2,10,10,activation_function = tf.nn.relu)

#add output layer
with tf.name_scope('output'):
    prediction = add_layer(h_l1,5,4,activation_function = tf.nn.softmax)

with tf.name_scope('loss'):
    cross_entropy = tf.reduce_mean(-tf.reduce_sum(ys*tf.log(prediction),reduction_indices=[1]))
    tf.scalar_summary('loss',cross_entropy)
with tf.name_scope('train'):
    train_step = tf.train.AdadeltaOptimizer(LEARNING_RATE).minimize(cross_entropy)
# tf.train.AdadeltaOptimizer
# tf.train.AdagradDAOptimizer
# tf.train.GradientDescentOptimizer
# tf.train.AdamOptimizer

with tf.Session() as sess:
    sess.run(tf.initialize_all_variables())

    xr, yr = labelData.random_validation(labelData.w_datalist)
    # print(sess.run(ys, feed_dict={xs: xr}))
    for i in range(2000):
        index = np.random.randint(0, data_x.shape[0], 128)
        loss, _, pred  = sess.run([cross_entropy, train_step, h_l1],feed_dict={xs:data_x, ys:data_y})
        if i % 100 == 0:
            print "Accuracy",compute_accuracy(xr, yr)
            print "Prediction",prediction_classification(testdata)[:50]
            # print mnist.test.labels
            # print len(mnist.test.images)
            print "loss is %s" %loss
            print "-----"
            save_path = tf.train.Saver().save(sess, "my_net/save_net.ckpt")
