package com.tsoft.ai.simulatedannealing;

import com.tsoft.ai.simulatedannealing.engine.SimulatedAnnealingService;
import lombok.AllArgsConstructor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@AllArgsConstructor
public class Application {

    private SimulatedAnnealingService annealingService;

    public void execute() {
        DefaultCategoryDataset chartDs = new DefaultCategoryDataset();

        try (BufferedWriter out = new BufferedWriter(new FileWriter("application.log"))) {
            out.write("step;temperature;energy;% worse accepted\n");

            annealingService.findSolution((s) -> {
                try {
                    out.write(String.format("%d;%f;%f;%.0f%%\n",
                            s.getTimer(), s.getTemperature(), s.getEnergy(), s.getWorseAcceptedPercent()));

                    Integer ox = Math.round(s.getTimer() / 10f) * 10;
                    chartDs.addValue(s.getTemperature(), "temperature", ox);
                    chartDs.addValue(s.getEnergy(), "energy", ox);
                    chartDs.addValue(s.getWorseAcceptedPercent(), "% worse accepted", ox);
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            });
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }

        System.out.println("Generating a chart ...");
        JFreeChart lineChartObject = ChartFactory.createLineChart(
                "Energy (number of conflicts)","Iteration",
                "Temperature",
                chartDs,
                PlotOrientation.VERTICAL,
                true,false,false);

        int width = 640;    /* Width of the image */
        int height = 480;   /* Height of the image */

        File lineChart = new File( "LineChart.jpeg" );

        try {
            ChartUtils.saveChartAsJPEG(lineChart, lineChartObject, width, height);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
}
